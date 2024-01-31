package com.orangecheese.reports.core.http.encoder;

import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetParameterEncoder implements IParameterEncoder {
    @Override
    public byte[] encode(JsonObject json) {
        List<String> keyValuePairs = json
                .entrySet()
                .stream()
                .map(kvp -> {
                    String key = kvp.getKey();
                    String value = !kvp.getValue().isJsonNull() ? kvp.getValue().getAsString() : "";
                    String urlEncodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
                    return key + "=" + urlEncodedValue;
                })
                .toList();

        String joinedKeyValuePairs = String.join("&", keyValuePairs);

        return joinedKeyValuePairs.getBytes();
    }
}
