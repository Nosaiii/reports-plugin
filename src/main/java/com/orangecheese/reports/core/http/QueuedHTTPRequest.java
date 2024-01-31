package com.orangecheese.reports.core.http;

import com.orangecheese.reports.core.http.request.HTTPRequest;

import java.util.function.Consumer;

public class QueuedHTTPRequest {
    private final HTTPRequest request;

    private final Consumer<Boolean> onRequestDone;

    public QueuedHTTPRequest(HTTPRequest request, Consumer<Boolean> onRequestDone) {
        this.request = request;
        this.onRequestDone = onRequestDone;
    }

    public HTTPRequest getRequest() {
        return request;
    }

    public void consumeOnRequestDone(boolean success) {
        onRequestDone.accept(success);
    }
}