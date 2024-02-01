package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.utility.PlayerUtility;
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
    private final PlayerProfile playerProfile;

    public ReadPlayerChatHistoryItem(Window context, UUID playerUuid) {
        super(context);
        this.playerProfile = PlayerUtility.getProfile(playerUuid);

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

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "Request the chat history of " + playerProfile.getName() + ".");
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}
