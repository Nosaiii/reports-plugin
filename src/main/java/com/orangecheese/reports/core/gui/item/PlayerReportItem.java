package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.data.ReportAction;
import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerProfile;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class PlayerReportItem extends ReportItem {
    public PlayerReportItem(Window context, int id, UUID reporterUuid, String message, boolean resolved, Date submissionDate, UUID playerUuid) {
        super(context, id, reporterUuid, message, resolved, submissionDate);

        OfflinePlayer cachedOfflinePlayer = Bukkit.getServer().getOfflinePlayer(playerUuid);
        PlayerProfile profile;
        if(cachedOfflinePlayer.getName() != null)
            profile = cachedOfflinePlayer.getPlayerProfile();
        else
            profile = PlayerUtility.getProfile(playerUuid);
        setTeleportReportAction(profile);

        addAdditionalArgument("Reported player", profile.getName());

        String status = Bukkit.getServer().getOfflinePlayer(Objects.requireNonNull(profile.getUniqueId())).isOnline() ? "ONLINE" : "OFFLINE";
        addAdditionalArgument("Status", status);
    }

    private void setTeleportReportAction(PlayerProfile reportedPlayerProfile) {
        addReportAction(ClickType.DROP, new ReportAction(
                "\"Q\" to teleport to " + reportedPlayerProfile.getName() + ".",
                (item, player) -> {
                    OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(Objects.requireNonNull(reportedPlayerProfile.getUniqueId()));
                    if(targetPlayer.getPlayer() == null || !targetPlayer.isOnline()) {
                        player.sendMessage(ChatColor.RED + reportedPlayerProfile.getName() + " is not online!");
                        player.closeInventory();
                        return;
                    }
                    player.teleport(targetPlayer.getPlayer().getLocation());
                }
        ));
    }

    @Override
    public ItemStack update(Player player) {
        return buildInitial(player);
    }
}