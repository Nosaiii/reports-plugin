package com.orangecheese.reports.core.http.request;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HTTPRequest {
    private final String url;

    private final HTTPMethod method;

    private final HashMap<String, String> headers;

    private final Runnable onSuccess;

    private final Runnable onFailure;

    public HTTPRequest(String url, HTTPMethod method, Runnable onSuccess, Runnable onFailure) {
        Pattern urlPattern = Pattern.compile("^(?:/)?[-a-zA-Z0-9@:%_\\+.~#?&//=]*$", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = urlPattern.matcher(url);

        if(!urlMatcher.find())
            throw new IllegalArgumentException("Invalid URL");

        this.url = urlMatcher.group();
        this.method = method;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;

        headers = new HashMap<>();
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public void invokeSuccess() {
        if(onSuccess == null)
            return;
        onSuccess.run();
    }

    public void invokeFailure() {
        if(onFailure == null)
            return;
        onFailure.run();
    }

    public String getUrl() {
        return url;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}