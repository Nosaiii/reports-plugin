package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public class CreateSuggestionReportRequest extends CreateReportRequest {
    public CreateSuggestionReportRequest(String accessToken, Player player, String message, Runnable onSuccess) {
        super("create-suggestion-report", accessToken, player, message, onSuccess);
    }

    @Override
    protected JsonObject appendJson(JsonObject json) {
        return json;
    }
}
