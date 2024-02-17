package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.utility.EmptyRecord;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class DeleteReportRequest extends HTTPRequestWithResponse<EmptyRecord, MessageResponse> implements IHTTPBody {
    private final String accessToken;

    private final int id;

    public DeleteReportRequest(
            String accessToken,
            int id,
            Runnable onSuccess,
            Consumer<MessageResponse> onFailure) {
        super(
                "report/delete",
                HTTPMethod.DELETE,
                response -> onSuccess.run(),
                onFailure);
        this.accessToken = accessToken;
        this.id = id;
    }

    @Override
    public EmptyRecord parseResponse(JsonObject json) {
        return new EmptyRecord();
    }

    @Override
    public MessageResponse parseFailureResponse(JsonObject json) {
        return new MessageResponse(json.get("message").getAsString());
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", accessToken);
        json.addProperty("id", id);
        return json;
    }
}
