package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public class CreateSuggestionReportRequest extends CreateReportRequest {
    public CreateSuggestionReportRequest(String accessToken, Player player, String message, boolean anonymous, Runnable onSuccess) {
        super("create-suggestion-report", accessToken, player, message, anonymous,  onSuccess);
    }

    @Override
    protected JsonObject appendJson(JsonObject json) {
        return json;
    }
}
