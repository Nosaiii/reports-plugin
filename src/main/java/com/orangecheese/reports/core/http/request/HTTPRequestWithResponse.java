package com.orangecheese.reports.core.http.request;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public abstract class HTTPRequestWithResponse<T, U> extends HTTPRequest {
    private final Consumer<T> onSuccess;

    private final Consumer<U> onFailure;

    public HTTPRequestWithResponse(String url, HTTPMethod method, Consumer<T> onSuccess, Consumer<U> onFailure) {
        super(url, method, null, null);
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    public abstract T parseResponse(JsonObject json);

    public abstract U parseFailureResponse(JsonObject json);

    public void processStream(InputStream stream) throws IOException {
        JsonObject json = streamToJson(stream);
        T parsedResponse = parseResponse(json);
        onSuccess.accept(parsedResponse);
    }

    public JsonObject processFailureStream(InputStream stream) throws IOException {
        JsonObject json = streamToJson(stream);
        U parsedResponse = parseFailureResponse(json);
        onFailure.accept(parsedResponse);
        return json;
    }

    private JsonObject streamToJson(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while((inputLine = reader.readLine()) != null)
            response.append(inputLine);
        reader.close();

        String jsonString = "{}";
        if(!response.isEmpty())
            jsonString = response.toString();

        return JsonParser.parseString(jsonString).getAsJsonObject();
    }
}