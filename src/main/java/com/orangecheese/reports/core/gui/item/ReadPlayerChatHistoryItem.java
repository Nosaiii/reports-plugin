package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.service.PlayerProfileService;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadPlayerChatHistoryItem extends WindowItem {
    private PlayerProfile playerProfile;

    public ReadPlayerChatHistoryItem(Window context, UUID playerUuid) {
        super(context);

        PlayerProfileService playerProfileService = ServiceContainer.get(PlayerProfileService.class);
        Player contextPlayer = context.getPlayer();
        playerProfileService.get(contextPlayer, playerUuid, profile -> {
            playerProfile = profile;
            notifyUpdate(contextPlayer);
        });

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            player.performCommand("reports chathistory " + playerUuid + " 1");
            player.closeInventory();
        });
    }

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Read chat history");

            String playerName = "(Fetching...)";
            if(playerProfile != null)
                playerName = playerProfile.getName();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "Request the chat history of " + playerName + ".");
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}
