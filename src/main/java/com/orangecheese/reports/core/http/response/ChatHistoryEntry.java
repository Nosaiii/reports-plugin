package com.orangecheese.reports.core.http.response;

import java.util.Date;
import java.util.UUID;

public class ChatHistoryEntry {
    private final int id;

    private final int containerId;

    private final UUID playerUuid;

    private final String message;

    private final Date createdAt;

    private final Date updatedAt;

    public ChatHistoryEntry(int id, int containerId, UUID playerUuid, String message, Date createdAt, Date updatedAt) {
        this.id = id;
        this.containerId = containerId;
        this.playerUuid = playerUuid;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public int getContainerId() {
        return containerId;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}