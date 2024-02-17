package com.orangecheese.reports.core.http.request;

public enum HTTPMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String value;

    HTTPMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}