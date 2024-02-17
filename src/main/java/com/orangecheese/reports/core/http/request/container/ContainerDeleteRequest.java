package com.orangecheese.reports.core.http.request.container;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.utility.EmptyRecord;

import java.util.function.Consumer;

public class ContainerDeleteRequest extends HTTPRequestWithResponse<EmptyRecord, MessageResponse> implements IHTTPBody {
    private final String identification;

    private final String keyPhrase;

    public ContainerDeleteRequest(
            String identification,
            String keyPhrase,
            Runnable onSuccess,
            Consumer<MessageResponse> onFailure) {
        super("container/delete", HTTPMethod.DELETE, body -> onSuccess.run(), onFailure);
        this.identification = identification;
        this.keyPhrase = keyPhrase;
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
        json.addProperty("identification", identification);
        json.addProperty("keyPhrase", keyPhrase);
        return json;
    }
}
