package com.orangecheese.reports.core.gui.item.abstraction;

import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.window.ReportActionWindow;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.service.PlayerProfileService;
import com.orangecheese.reports.utility.DateUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.util.ChatPaginator;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class ReportItem<T> extends WindowItem {
    protected final ReportData<T> reportData;

    private PlayerProfile reporterProfile;

    private final Map<String, String> additionalAttributes;

    private static final int LINE_LENGTH = 45;

    public ReportItem(Window context, ReportData<T> reportData) {
        super(context);
        this.reportData = reportData;

        PlayerProfileService playerProfileService = ServiceContainer.get(PlayerProfileService.class);
        Player contextPlayer = context.getPlayer();
        playerProfileService.get(
                contextPlayer,
                reportData.getReporterUuid(),
                profile -> {
                    reporterProfile = profile;
                    notifyUpdate(contextPlayer);
                });

        additionalAttributes = new LinkedHashMap<>();
        setAdditionalArgument("Message", reportData.getMessage());

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            ReportActionWindow<T> actionWindow = new ReportActionWindow<>(player, context.getHistory(), reportData);

            for(WindowItem additionalOption : buildAdditionalOptions(actionWindow))
                actionWindow.addAdditionalOption(additionalOption);

            actionWindow.open();
        });
    }

    public abstract ArrayList<WindowItem> buildAdditionalOptions(Window context);

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if(meta != null) {
            String reportedByLineValue = ChatColor.ITALIC + "Anonymous";
            if(reporterProfile != null) {
                meta.setOwnerProfile(reporterProfile);

                OfflinePlayer reporterOfflinePlayer = Bukkit.getServer().getOfflinePlayer(reportData.getReporterUuid());

                reportedByLineValue =
                        reporterProfile.getName() +
                        " " +
                        "[" + (reporterOfflinePlayer.isOnline() ? ChatColor.GREEN : ChatColor.DARK_RED) + "⏺" + ChatColor.DARK_GRAY + "]";
            }

            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + reportData.getId());

            List<String> lore = new ArrayList<>();

            String reportedByLine = ChatColor.DARK_GRAY + "Reported by " + reportedByLineValue;
            lore.add(reportedByLine);

            String timezone = ReportsPlugin.getInstance().getReportsConfig().getLocalization().getTimezone();
            LocalDateTime timezoneAwareDateTime = DateUtility.convertFromGMT(reportData.getCreatedAt(), timezone);

            DateTimeFormatter submissionDateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
            DateTimeFormatter submissionTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

            String dateString = submissionDateFormat.format(timezoneAwareDateTime);
            String timeString = submissionTimeFormat.format(timezoneAwareDateTime);
            lore.add(ChatColor.DARK_GRAY + dateString + " at " + timeString);

            if(!additionalAttributes.isEmpty()) {
                lore.add("");
                for(String attributeKey : additionalAttributes.keySet()) {
                    String attributeValue = additionalAttributes.get(attributeKey);
                    String line = ChatColor.GOLD + attributeKey + ": " + ChatColor.WHITE + attributeValue;
                    String[] wrappedLine = ChatPaginator.wordWrap(line, LINE_LENGTH);
                    lore.addAll(List.of(wrappedLine));
                }
            }

            lore.add("");
            lore.add(ChatColor.BLUE + "Left-click for more options.");

            if(reportData.isResolved()) {
                lore.add("");
                lore.add(ChatColor.GREEN + "✓ Resolved");
            }

            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    protected void setAdditionalArgument(String key, String value) {
        additionalAttributes.put(key, value);
    }
}
