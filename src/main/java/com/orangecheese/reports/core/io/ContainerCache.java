package com.orangecheese.reports.core.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.binding.ServiceConstructor;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.container.ContainerAuthRequest;
import com.orangecheese.reports.core.http.response.ContainerAuthResponse;
import com.orangecheese.reports.core.http.response.MessageResponse;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ContainerCache {
    private final File file;

    private final APIManager apiManager;

    @ServiceConstructor
    public ContainerCache(APIManager apiManager) {
        this.apiManager = apiManager;

        String dataFolderPath = ReportsPlugin.getInstance().getDataFolder().getPath();
        Path path = Path.of(dataFolderPath, "container.json");
        file = path.toFile();
    }

    public void setAccessToken(String accessToken) {
        JsonObject json = parseFile();
        json.addProperty("accessToken", accessToken);

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try(FileWriter writer = new FileWriter(file)) {
            writer.write(json.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccessToken() {
        JsonObject json = parseFile();

        if(!json.has("accessToken"))
            return null;

        return json.get("accessToken").getAsString();
    }

    public void checkAuthentication(
            String identification,
            String keyPhrase,
            Consumer<ContainerAuthResponse> onSuccess,
            Consumer<MessageResponse> onFailure) {
        ContainerAuthRequest authRequest = new ContainerAuthRequest(identification, keyPhrase, onSuccess, onFailure);
        apiManager.makeRequest(authRequest);
    }

    private JsonObject parseFile() {
        if(!file.exists())
            return new JsonObject();

        try {
            String jsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            return JsonParser.parseString(jsonString).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}