package com.orangecheese.reports.event;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.playerprofile.PlayerProfileCreateRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerProfileRegistrationEvent implements Listener {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    public PlayerProfileRegistrationEvent() {
        apiManager = ServiceContainer.get(APIManager.class);
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerProfileCreateRequest profileCreateRequest = new PlayerProfileCreateRequest(player.getUniqueId(), containerCache.getAccessToken());
        apiManager.makeRequest(profileCreateRequest);
    }
}