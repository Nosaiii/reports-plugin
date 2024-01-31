package com.orangecheese.reports.core.http.request;

import com.google.gson.JsonObject;

public interface IHTTPBody {
    JsonObject generateJson();
}