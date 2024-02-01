package com.orangecheese.reports.core.http.request.playerprofile;

import com.google.gson.JsonObject;
import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequest;
import com.orangecheese.reports.core.http.request.IHTTPBody;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerProfileCreateRequest extends HTTPRequest implements IHTTPBody {
    private final UUID uuid;

    private final String accessToken;

    public PlayerProfileCreateRequest(
            UUID uuid,
            String accessToken) {
        super(
                "player-profile/create",
                HTTPMethod.POST,
                () -> {},
                () -> ReportsPlugin.getInstance().getLogger().log(Level.SEVERE, "Something went wrong submitting a chat history entry! Is the plugin linked to a container?"));
        this.uuid = uuid;
        this.accessToken = accessToken;
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", accessToken);
        json.addProperty("uuid", uuid.toString());
        return json;
    }
}
