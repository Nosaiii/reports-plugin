package com.orangecheese.reports.core.http.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReportData<T> {
    private final int id;

    private final int containerId;

    private final UUID reporterUuid;

    private final String message;

    private boolean resolved;

    private final Date createdAt;

    private final Date updatedAt;

    private final T attributes;

    public ReportData(
            int id,
            int containerId,
            UUID reporterUuid,
            String message,
            boolean resolved,
            Date createdAt,
            Date updatedAt,
            T attributes) {
        this.id = id;
        this.containerId = containerId;
        this.reporterUuid = reporterUuid;
        this.message = message;
        this.resolved = resolved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = attributes;
    }

    public static <T> ReportData<T> fromJson(JsonObject json, T attributes) {
        int id = json.get("id").getAsInt();
        int containerId = json.get("container_id").getAsInt();

        JsonElement reporterUuidElement = json.get("reporter_uuid");
        UUID reporterUuid = !reporterUuidElement.isJsonNull() ? UUID.fromString(reporterUuidElement.getAsString()) : null;

        String message = json.get("message").getAsString();
        boolean resolved = json.get("resolved").getAsBoolean();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        Date createdAt;
        Date updatedAt;
        try {
            createdAt = dateFormat.parse(json.get("created_at").getAsString());
            updatedAt = dateFormat.parse(json.get("updated_at").getAsString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return new ReportData<>(id, containerId, reporterUuid, message, resolved, createdAt, updatedAt, attributes);
    }

    public int getId() {
        return id;
    }

    public int getContainerId() {
        return containerId;
    }

    public UUID getReporterUuid() {
        return reporterUuid;
    }

    public String getMessage() {
        return message;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isResolved() {
        return resolved;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public T getAttributes() {
        return attributes;
    }
}