package com.orangecheese.reports.core.http.encoder;

import com.google.gson.JsonObject;

public class PostParameterEncoder implements IParameterEncoder {
    @Override
    public byte[] encode(JsonObject json) {
        String jsonData = json.toString();
        return jsonData.getBytes();
    }
}
