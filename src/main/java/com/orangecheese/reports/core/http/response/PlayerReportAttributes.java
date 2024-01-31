package com.orangecheese.reports.core.http.response;

import java.util.UUID;

public class PlayerReportAttributes {
    private final int id;

    private final UUID playerUuid;

    public PlayerReportAttributes(int id, UUID playerUuid) {
        this.id = id;
        this.playerUuid = playerUuid;
    }

    public int getId() {
        return id;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
}