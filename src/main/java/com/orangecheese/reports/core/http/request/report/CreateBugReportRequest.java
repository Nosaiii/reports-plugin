package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public class CreateBugReportRequest extends CreateReportRequest {
    private final String stepsToReproduce;

    public CreateBugReportRequest(String accessToken, Player player, String message, boolean anonymous, String stepsToReproduce, Runnable onSuccess) {
        super("create-bug-report", accessToken, player, message, anonymous, onSuccess);
        this.stepsToReproduce = stepsToReproduce;
    }

    @Override
    protected JsonObject appendJson(JsonObject json) {
        json.addProperty("stepsToReproduce", stepsToReproduce);
        return json;
    }
}
