package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.PlayerReportAttributes;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PlayerReportItem extends ReportItem<PlayerReportAttributes> {
    public PlayerReportItem(Window context, ReportData<PlayerReportAttributes> reportData) {
        super(context, reportData);

        UUID playerUuid = reportData.getAttributes().getPlayerUuid();

        OfflinePlayer cachedOfflinePlayer = Bukkit.getServer().getOfflinePlayer(playerUuid);
        PlayerProfile profile;
        if(cachedOfflinePlayer.getName() != null)
            profile = cachedOfflinePlayer.getPlayerProfile();
        else
            profile = PlayerUtility.getProfile(playerUuid);

        addAdditionalArgument("Reported player", profile.getName());

        String status = Bukkit.getServer().getOfflinePlayer(Objects.requireNonNull(profile.getUniqueId())).isOnline() ? "ONLINE" : "OFFLINE";
        addAdditionalArgument("Status", status);
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