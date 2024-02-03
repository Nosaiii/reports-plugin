package com.orangecheese.reports.core.http.request.container;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.ContainerAuthResponse;
import com.orangecheese.reports.core.http.response.MessageResponse;

import java.util.UUID;
import java.util.function.Consumer;

public class ContainerRegisterRequest extends HTTPRequestWithResponse<ContainerAuthResponse, MessageResponse> implements IHTTPBody {
    private final String identification;

    private final String keyPhrase;

    private final UUID ownerUuid;

    public ContainerRegisterRequest(
            String identification,
            String keyPhrase,
            UUID ownerUuid,
            Consumer<ContainerAuthResponse> onSuccess,
            Consumer<MessageResponse> onFailure) {
        super("container/register", HTTPMethod.POST, onSuccess, onFailure);
        this.identification = identification;
        this.keyPhrase = keyPhrase;
        this.ownerUuid = ownerUuid;
    }

    @Override
    public ContainerAuthResponse parseResponse(JsonObject json) {
        String token = json.get("token").getAsString();
        String expiresAtString = json.get("expires_at").getAsString();
        return new ContainerAuthResponse(token, expiresAtString);
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
        json.addProperty("ownerUuid", ownerUuid.toString());
        return json;
    }
}
