package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.PlayerReportAttributes;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.service.PlayerProfileService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PlayerReportItem extends ReportItem<PlayerReportAttributes> {
    public PlayerReportItem(Window context, ReportData<PlayerReportAttributes> reportData) {
        super(context, reportData);

        UUID playerUuid = reportData.getAttributes().getPlayerUuid();

        PlayerProfileService playerProfileService = ServiceContainer.get(PlayerProfileService.class);
        Player contextPlayer = context.getPlayer();
        playerProfileService.get(contextPlayer, playerUuid, profile -> {
            setAdditionalArgument("Reported player", profile.getName());

            String status = Bukkit.getServer().getOfflinePlayer(Objects.requireNonNull(profile.getUniqueId())).isOnline() ? "ONLINE" : "OFFLINE";
            setAdditionalArgument("Status", status);

            notifyUpdate(contextPlayer);
        });

        setAdditionalArgument("Reported player", "(Fetching...)");
        setAdditionalArgument("Status", "(Fetching...)");
    }

    @Override
    public ArrayList<WindowItem> buildAdditionalOptions(Window context) {
        ArrayList<WindowItem> options = new ArrayList<>();

        UUID playerUuid = reportData.getAttributes().getPlayerUuid();

        TeleportToPlayerItem teleportToReporterItem = new TeleportToPlayerItem(context, playerUuid, "Teleport to reported player");
        options.add(teleportToReporterItem);

        ReadPlayerChatHistoryItem chatHistoryItem = new ReadPlayerChatHistoryItem(context, playerUuid);
        options.add(chatHistoryItem);

        return options;
    }
}