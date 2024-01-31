package com.orangecheese.reports.core.http;

public enum HTTPProtocol {
    HTTP("http"),
    HTTPS("https");

    private final String name;

    HTTPProtocol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}