package com.orangecheese.reports.core.http.encoder;

import com.google.gson.JsonObject;

public interface IParameterEncoder {
    byte[] encode(JsonObject json);
}