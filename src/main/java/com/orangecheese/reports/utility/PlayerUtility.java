package com.orangecheese.reports.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.utils.URIBuilder;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

public final class PlayerUtility {
    public static PlayerProfile getProfile(String name) {
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("https")
                .setHost("api.mojang.com")
                .setPathSegments(Arrays.asList(
                        "users",
                        "profiles",
                        "minecraft",
                        name
                ));

        JsonObject jsonResponse;
        try {
            jsonResponse = readFromGetRequest(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String jsonId = jsonResponse.get("id").getAsString();
        String jsonName = jsonResponse.get("name").getAsString();

        return Bukkit.getServer().createPlayerProfile(UUID.fromString(uuidToDashedUuid(jsonId)), jsonName);
    }

    public static PlayerProfile getProfile(UUID uuid) {
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("https")
                .setHost("api.mojang.com")
                .setPathSegments(Arrays.asList(
                        "user",
                        "profile",
                        uuid.toString()
                ));

        JsonObject jsonResponse;
        try {
            jsonResponse = readFromGetRequest(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String jsonName = jsonResponse.get("name").getAsString();
        String jsonId = jsonResponse.get("id").getAsString();

        return Bukkit.getServer().createPlayerProfile(UUID.fromString(uuidToDashedUuid(jsonId)), jsonName);
    }

    public static String uuidToDashedUuid(String uuid) {
        return uuid.replaceAll(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5");
    }

    private static JsonObject readFromGetRequest(URI uri) {
        try {
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            if(responseCode == 404)
                return null;
            else if(connection.getResponseCode() != 200)
                throw new IOException("request returned " + connection.getResponseCode());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = reader.readLine()) != null)
                content.append(inputLine);
            reader.close();

            return JsonParser.parseString(content.toString()).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}