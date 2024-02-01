package com.orangecheese.reports.service;

import com.orangecheese.reports.binding.ServiceConstructor;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.playerprofile.PlayerProfileFetchByUUIDRequest;
import com.orangecheese.reports.core.http.request.playerprofile.PlayerProfileFetchByUsernameRequest;
import com.orangecheese.reports.core.http.response.MessageResponse;
import com.orangecheese.reports.core.io.ContainerCache;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.util.UUID;
import java.util.function.Consumer;

public final class PlayerProfileService {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    @ServiceConstructor
    public PlayerProfileService(APIManager apiManager, ContainerCache containerCache) {
        this.apiManager = apiManager;
        this.containerCache = containerCache;
    }

    public void get(Player player, UUID uuid, Consumer<PlayerProfile> onSuccess) {
        PlayerProfileFetchByUUIDRequest request = new PlayerProfileFetchByUUIDRequest(player, uuid, containerCache.getAccessToken(), onSuccess);
        apiManager.makeRequest(request);
    }

    public void get(Player player, String username, Consumer<PlayerProfile> onSuccess) {
        PlayerProfileFetchByUsernameRequest request = new PlayerProfileFetchByUsernameRequest(player, username, containerCache.getAccessToken(), onSuccess);
        apiManager.makeRequest(request);
    }

    public void getWithCatch(UUID uuid, Consumer<PlayerProfile> onSuccess, Consumer<MessageResponse> onFailure) {
        PlayerProfileFetchByUUIDRequest request = new PlayerProfileFetchByUUIDRequest(uuid, containerCache.getAccessToken(), onSuccess, onFailure);
        apiManager.makeRequest(request);
    }

    public void getWithCatch(String username, Consumer<PlayerProfile> onSuccess, Consumer<MessageResponse> onFailure) {
        PlayerProfileFetchByUsernameRequest request = new PlayerProfileFetchByUsernameRequest(username, containerCache.getAccessToken(), onSuccess, onFailure);
        apiManager.makeRequest(request);
    }
}