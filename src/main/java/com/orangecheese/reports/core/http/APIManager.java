package com.orangecheese.reports.core.http;

import com.google.gson.JsonObject;
import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.binding.ServiceConstructor;
import com.orangecheese.reports.config.APIConfig;
import com.orangecheese.reports.core.http.encoder.GetParameterEncoder;
import com.orangecheese.reports.core.http.encoder.IParameterEncoder;
import com.orangecheese.reports.core.http.encoder.PostParameterEncoder;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequest;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.utility.ThreadUtility;
import org.apache.http.client.utils.URIBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class APIManager {
    private final HTTPProtocol protocol;

    private final String hostName;

    public final int port;

    public final int apiVersion;

    private final URL versionedBaseUrl;

    private Runnable failedConnectionHandler;

    private final HashMap<Integer, IParameterEncoder> encoders;

    private final Queue<QueuedHTTPRequest> requestQueue;

    private QueuedHTTPRequest currentlyProcessedRequest;

    private static final Executor SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor();

    @ServiceConstructor
    public APIManager() {
        APIConfig apiConfig = ReportsPlugin.getInstance().getReportsConfig().getApi();

        protocol = apiConfig.getProtocol();
        hostName = apiConfig.getHostName();
        port = apiConfig.getPort();
        apiVersion = apiConfig.getVersion();

        try {
            versionedBaseUrl = new URL(protocol.getName() + "://" + hostName + ":" + port + "/api/v" + apiVersion + "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        encoders = new HashMap<>();
        encoders.put(HTTPMethod.GET.ordinal(), new GetParameterEncoder());
        encoders.put(HTTPMethod.POST.ordinal() | HTTPMethod.DELETE.ordinal(), new PostParameterEncoder());

        requestQueue = new LinkedList<>();
    }

    public void setFailedConnectionHandler(Runnable runnable) {
        failedConnectionHandler = runnable;
    }

    public boolean ping() {
        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(hostName, port), 10000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void queueRequest(HTTPRequest request, Consumer<Boolean> onRequestDone) {
        QueuedHTTPRequest queuedRequest = new QueuedHTTPRequest(request, onRequestDone);
        requestQueue.add(queuedRequest);

        if(currentlyProcessedRequest == null)
            dequeueRequest();
    }

    private void dequeueRequest() {
        QueuedHTTPRequest request = requestQueue.poll();
        if(request == null)
            return;

        currentlyProcessedRequest = request;

        makeRequest(request.getRequest()).thenAccept(success -> {
            request.consumeOnRequestDone(true);
            currentlyProcessedRequest = null;
        });
    }

    public CompletableFuture<Boolean> makeRequest(HTTPRequest request) {
        CompletableFuture<RequestResult> resultTask = executeRequest(request);

        return resultTask.thenApply(result -> {
            if(!result.isSuccess()) {
                ThreadUtility.executeOnMainThread(() -> {
                    try {
                        InputStream inputStream = result.getStream();
                        JsonObject jsonResponse = null;

                        if (request instanceof HTTPRequestWithResponse<?, ?> requestResponse) {
                            jsonResponse = requestResponse.processFailureStream(inputStream);
                        } else {
                            request.invokeFailure();
                        }

                        if(ReportsPlugin.getInstance().getReportsConfig().getDebug().isApiShowErrorInConsole()) {
                            String errorResponse;
                            if (jsonResponse == null)
                                errorResponse = "null";
                            else
                                errorResponse = jsonResponse.toString();

                            Bukkit.getConsoleSender().sendMessage(
                                    ChatColor.RED + "ERROR!" + ChatColor.WHITE + " An error occurred trying to make a request to the endpoint.",
                                    ChatColor.WHITE + "{endpoint:\"" + request.getUrl() + "\",status_code:" + result.getStatusCode() + ",response:" + errorResponse + "}"
                            );
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                return false;
            }

            ThreadUtility.executeOnMainThread(() -> {
                if(request instanceof HTTPRequestWithResponse<?, ?> requestResponse) {
                    try {
                        requestResponse.processStream(result.getStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    request.invokeSuccess();
                }
            });

            return true;
        });
    }

    private CompletableFuture<RequestResult> executeRequest(HTTPRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            if(ReportsPlugin.getInstance().getReportsConfig().getDebug().isApiLogOnRequest())
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "Requesting to endpoint '" + request.getUrl() + "'...");

            try {
                URI requestUri = versionedBaseUrl.toURI().resolve("./" + request.getUrl());

                if(request instanceof IHTTPBody requestBody && request.getMethod() == HTTPMethod.GET) {
                    JsonObject requestBodyJson = requestBody.generateJson();
                    byte[] bodyBytes = encoders.get(HTTPMethod.GET.ordinal()).encode(requestBodyJson);

                    String queryString = new String(bodyBytes, StandardCharsets.UTF_8);
                    requestUri = new URIBuilder(requestUri).setCustomQuery(queryString).build();
                }

                HttpURLConnection connection = (HttpURLConnection) requestUri.toURL().openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod(request.getMethod().getValue());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                if(request instanceof IHTTPBody requestBody && request.getMethod() != HTTPMethod.GET) {
                    JsonObject requestBodyJson = requestBody.generateJson();
                    byte[] bodyBytes = encoders.get(HTTPMethod.POST.ordinal() | HTTPMethod.DELETE.ordinal()).encode(requestBodyJson);

                    connection.setDoOutput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(bodyBytes);
                    outputStream.flush();
                    outputStream.close();
                }

                int statusCode = connection.getResponseCode();
                if(statusCode >= 400 && statusCode <= 599)
                    return RequestResult.fromFailure(statusCode, connection.getErrorStream());

                InputStream responseStream;
                try {
                    responseStream = connection.getInputStream();
                } catch(IOException e) {
                    return RequestResult.fromFailure(statusCode, connection.getErrorStream());
                }

                return RequestResult.fromSuccess(statusCode, responseStream);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                failedConnectionHandler.run();
                return RequestResult.fromFailure(500, null);
            }
        }, SINGLE_THREAD_EXECUTOR);
    }

    public HTTPProtocol getProtocol() {
        return protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public int getAPIVersion() {
        return apiVersion;
    }
}