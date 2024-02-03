package com.orangecheese.reports.core.http.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class ReportData<T> {
    private final int id;

    private final int containerId;

    private final UUID reporterUuid;

    private final String message;

    private boolean resolved;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final T attributes;

    public ReportData(
            int id,
            int containerId,
            UUID reporterUuid,
            String message,
            boolean resolved,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
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

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        LocalDateTime createdAt, updatedAt;
        try {
            createdAt = LocalDateTime.parse(json.get("created_at").getAsString(), dateFormat);
            updatedAt = LocalDateTime.parse(json.get("updated_at").getAsString(), dateFormat);
        } catch (DateTimeParseException e) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public T getAttributes() {
        return attributes;
    }
}