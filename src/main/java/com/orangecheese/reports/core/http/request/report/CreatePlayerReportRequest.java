package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreatePlayerReportRequest extends CreateReportRequest {
    private final UUID playerUuid;

    public CreatePlayerReportRequest(String accessToken, Player player, String message, UUID playerUuid, Runnable onSuccess) {
        super("create-player-report", accessToken, player, message, onSuccess);
        this.playerUuid = playerUuid;
    }

    @Override
    protected JsonObject appendJson(JsonObject json) {
        json.addProperty("playerUuid", playerUuid.toString());
        return json;
    }
}
