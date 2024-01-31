package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.core.http.response.ReportsMetaResponse;
import com.orangecheese.reports.core.io.ContainerCache;

import java.util.function.Consumer;

public class ReadReportsMetaRequest extends HTTPRequestWithResponse<ReportsMetaResponse, MessageResponse> implements IHTTPBody {
    private final ContainerCache containerCache;

    public ReadReportsMetaRequest(Consumer<ReportsMetaResponse> onSuccess, Consumer<MessageResponse> onFailure) {
        super("report/meta", HTTPMethod.GET, onSuccess, onFailure);
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    public ReportsMetaResponse parseResponse(JsonObject json) {
        int bugReports = json.get("bug_reports").getAsInt();
        int playerReports = json.get("player_reports").getAsInt();
        int suggestionReports = json.get("suggestion_reports").getAsInt();
        return new ReportsMetaResponse(bugReports, playerReports, suggestionReports);
    }

    @Override
    public MessageResponse parseFailureResponse(JsonObject json) {
        return new MessageResponse(json.get("message").getAsString());
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", containerCache.getAccessToken());
        return json;
    }
}
