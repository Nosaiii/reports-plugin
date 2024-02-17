package com.orangecheese.reports.core.http.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatHistoryEntry {
    private final int id;

    private final int containerId;

    private final UUID playerUuid;

    private final String message;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public ChatHistoryEntry(int id, int containerId, UUID playerUuid, String message, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}