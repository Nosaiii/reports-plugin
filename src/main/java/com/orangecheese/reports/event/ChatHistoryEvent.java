package com.orangecheese.reports.event;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.chathistory.ChatHistoryCreateRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHistoryEvent implements Listener {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    public ChatHistoryEvent() {
        apiManager = ServiceContainer.get(APIManager.class);
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(event.isCancelled())
            return;

        ChatHistoryCreateRequest createRequest = new ChatHistoryCreateRequest(
                player.getUniqueId(),
                event.getMessage(),
                containerCache.getAccessToken()
        );
        apiManager.makeRequest(createRequest);
    }
}