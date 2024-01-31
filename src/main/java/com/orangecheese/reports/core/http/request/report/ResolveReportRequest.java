package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.utility.EmptyRecord;

import java.util.function.Consumer;

public class ResolveReportRequest extends HTTPRequestWithResponse<EmptyRecord, MessageResponse> implements IHTTPBody {
    private final String accessToken;

    private final int id;

    private final boolean resolved;

    public ResolveReportRequest(String accessToken, int id, boolean resolved, Runnable onSuccess, Consumer<MessageResponse> onFailure) {
        super("report/resolve", HTTPMethod.POST, body -> onSuccess.run(), onFailure);
        this.accessToken = accessToken;
        this.id = id;
        this.resolved = resolved;
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
        json.addProperty("reportBaseId", id);
        json.addProperty("resolved", resolved);
        return json;
    }
}
