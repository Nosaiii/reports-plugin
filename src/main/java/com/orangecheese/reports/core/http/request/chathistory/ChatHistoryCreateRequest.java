package com.orangecheese.reports.core.http.request.chathistory;

import com.google.gson.JsonObject;
import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequest;
import com.orangecheese.reports.core.http.request.IHTTPBody;

import java.util.UUID;
import java.util.logging.Level;

public class ChatHistoryCreateRequest extends HTTPRequest implements IHTTPBody {
    private final UUID playerUuid;

    private final String message;

    private final String accessToken;

    public ChatHistoryCreateRequest(
            UUID playerUuid,
            String message,
            String accessToken) {
        super(
                "chat-history/create",
                HTTPMethod.POST,
                () -> {},
                () -> ReportsPlugin.getInstance().getLogger().log(Level.SEVERE, "Something went wrong submitting a chat history entry! Is the plugin linked to a container?"));
        this.playerUuid = playerUuid;
        this.message = message;
        this.accessToken = accessToken;
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", accessToken);
        json.addProperty("playerUuid", playerUuid.toString());
        json.addProperty("message", message);
        return json;
    }
}
