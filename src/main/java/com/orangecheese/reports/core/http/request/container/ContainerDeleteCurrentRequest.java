package com.orangecheese.reports.core.http.request.container;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.utility.EmptyRecord;

import java.util.function.Consumer;

public class ContainerDeleteCurrentRequest extends HTTPRequestWithResponse<EmptyRecord, MessageResponse> implements IHTTPBody {
    private final String accessToken;

    public ContainerDeleteCurrentRequest(
            String accessToken,
            Runnable onSuccess,
            Consumer<MessageResponse> onFailure) {
        super("container/delete-current", HTTPMethod.DELETE, body -> onSuccess.run(), onFailure);
        this.accessToken = accessToken;
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
        return json;
    }
}
