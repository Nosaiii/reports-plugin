package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.utility.EmptyRecord;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class CreateReportRequest extends HTTPRequestWithResponse<EmptyRecord, MessageResponse> implements IHTTPBody {
    private final String accessToken;

    private final Player player;

    private final String message;

    private final boolean anonymous;

    public CreateReportRequest(
            String endpoint,
            String accessToken,
            Player player,
            String message,
            boolean anonymous,
            Runnable onSuccess) {
        super(
                "report/" + endpoint,
                HTTPMethod.POST,
                response -> onSuccess.run(),
                response -> player.sendMessage(ChatColor.RED + "An unknown error occurred while trying to process your report submission: " + response.getMessage()));
        this.accessToken = accessToken;
        this.player = player;
        this.message = message;
        this.anonymous = anonymous;
    }

    @Override
    public EmptyRecord parseResponse(JsonObject json) {
        return new EmptyRecord();
    }

    @Override
    public MessageResponse parseFailureResponse(JsonObject json) {
        return new MessageResponse(json.get("message").getAsString());
    }

    protected abstract JsonObject appendJson(JsonObject json);

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", accessToken);
        if(!anonymous)
            json.addProperty("reporterUuid", player.getUniqueId().toString());
        json.addProperty("message", message);

        json = appendJson(json);

        return json;
    }
}
