package com.orangecheese.reports.core.http;

import java.io.InputStream;

public class RequestResult {
    private final boolean succeeded;

    private final int statusCode;

    private final InputStream stream;

    private RequestResult(boolean succeeded, int statusCode, InputStream stream) {
        this.succeeded = succeeded;
        this.statusCode = statusCode;
        this.stream = stream;
    }

    public static RequestResult fromSuccess(int statusCode, InputStream stream) {
        return new RequestResult(true, statusCode, stream);
    }

    public static RequestResult fromFailure(int statusCode, InputStream stream) {
        return new RequestResult(false, statusCode, stream);
    }

    public boolean isSuccess() {
        return succeeded;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public InputStream getStream() {
        return stream;
    }
}