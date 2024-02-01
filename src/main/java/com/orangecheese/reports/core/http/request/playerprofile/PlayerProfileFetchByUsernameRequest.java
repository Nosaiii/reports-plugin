package com.orangecheese.reports.core.http.request.playerprofile;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerProfileFetchByUsernameRequest extends HTTPRequestWithResponse<PlayerProfile, MessageResponse> implements IHTTPBody {
    private final String username;

    private final String accessToken;

    public PlayerProfileFetchByUsernameRequest(
            String username,
            String accessToken,
            Consumer<PlayerProfile> onSuccess,
            Consumer<MessageResponse> onFailure) {
        super(
                "player-profile/fetch-by-username",
                HTTPMethod.GET,
                onSuccess,
                onFailure);
        this.username = username;
        this.accessToken = accessToken;
    }

    public PlayerProfileFetchByUsernameRequest(
            Player player,
            String username,
            String accessToken,
            Consumer<PlayerProfile> onSuccess) {
        this(
                username,
                accessToken,
                onSuccess,
                response -> player.sendMessage(ChatColor.RED + "Something went wrong while fetching player profiles: " + response.getMessage())
        );
    }

    @Override
    public PlayerProfile parseResponse(JsonObject json) {
        UUID uuid = UUID.fromString(PlayerUtility.uuidToDashedUuid(json.get("uuid").getAsString()));
        String username = json.get("username").getAsString();
        return Bukkit.getServer().createPlayerProfile(uuid, username);
    }

    @Override
    public MessageResponse parseFailureResponse(JsonObject json) {
        return new MessageResponse(json.get("message").getAsString());
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", accessToken);
        json.addProperty("username", username);
        return json;
    }
}
