package com.orangecheese.reports.core.http.response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContainerAuthResponse {
    private final String token;

    private final Date expiresAt;

    public ContainerAuthResponse(String token, Date expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public ContainerAuthResponse(String token, String expiresAtString) {
        this.token = token;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        try {
            this.expiresAt = dateFormat.parse(expiresAtString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken() {
        return token;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }
}