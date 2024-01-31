package com.orangecheese.reports.event;

import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatPromptEvent implements Listener {
    private final ChatPrompt prompt;

    public ChatPromptEvent(ChatPrompt prompt) {
        this.prompt = prompt;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(player != prompt.getPlayer())
            return;

        event.setCancelled(true);

        String message = event.getMessage();

        if(message.equalsIgnoreCase("cancel")) {
            prompt.cancel();
            return;
        }

        prompt.process(message);
    }
}