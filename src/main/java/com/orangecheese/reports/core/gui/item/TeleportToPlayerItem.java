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

public class TeleportToPlayerItem extends WindowItem {
    private PlayerProfile playerProfile;

    private final String itemLabel;

    public TeleportToPlayerItem(Window context, UUID playerUuid, String itemLabel) {
        super(context);

        PlayerProfileService playerProfileService = ServiceContainer.get(PlayerProfileService.class);
        Player contextPlayer = context.getPlayer();
        playerProfileService.get(contextPlayer, playerUuid, profile -> {
            playerProfile = profile;
            notifyUpdate(contextPlayer);
        });

        this.itemLabel = itemLabel;

        OfflinePlayer offlineReporter = Bukkit.getServer().getOfflinePlayer(playerUuid);

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            if (!offlineReporter.isOnline())
                return;
            Player reporter = offlineReporter.getPlayer();
            if (reporter == null)
                return;
            player.closeInventory();
            player.teleport(reporter.getLocation());
            player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 1.0f);
        });
    }

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(ChatColor.GOLD + itemLabel);

            String playerName = "(Fetching...)";
            if(playerProfile != null)
                playerName = playerProfile.getName();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "Teleport yourself to " + playerName + ".");
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}
