package com.orangecheese.reports.core.gui.item.abstraction;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.data.ReportAction;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.service.ReportService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.ChatPaginator;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class ReportItem extends WindowItem implements IAutoUpdatingWindowItem {
    private final int id;

    private final UUID reporterUuid;

    private final String message;

    private boolean resolved;

    private final Date submissionDate;

    private final Map<String, String> additionalAttributes;

    private final Map<ClickType, ReportAction> reportActions;

    private static final int LINE_LENGTH = 45;

    public ReportItem(Window context, int id, UUID reporterUuid, String message, boolean resolved, Date submissionDate) {
        super(context);
        this.id = id;
        this.reporterUuid = reporterUuid;
        this.message = message;
        this.resolved = resolved;
        this.submissionDate = submissionDate;

        additionalAttributes = new LinkedHashMap<>();
        addAdditionalArgument("Message", message);

        reportActions = new HashMap<>();

        OfflinePlayer offlineReporter = Bukkit.getServer().getOfflinePlayer(reporterUuid);

        addReportAction(ClickType.LEFT, new ReportAction(
                "Left-click to " + (resolved ? "unresolve" : "resolve") + " the report.",
                (item, player) -> resolve(player, !item.resolved)
        ));

        addReportAction(ClickType.MIDDLE, new ReportAction(
                "Middle-click to teleport to " + offlineReporter.getName(),
                (item, player) -> {
                    if(!offlineReporter.isOnline())
                        return;
                    Player reporter = offlineReporter.getPlayer();
                    if(reporter == null)
                        return;
                    player.closeInventory();
                    player.teleport(reporter.getLocation());
                }
        ));
    }

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if(meta != null) {
            OfflinePlayer reporter = Bukkit.getServer().getOfflinePlayer(reporterUuid);
            meta.setOwningPlayer(reporter);

            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + id);

            List<String> lore = new ArrayList<>();
            String reportedByLine = ChatColor.DARK_GRAY + "Reported by " + reporter.getName() +
                    " " +
                    "[" + (reporter.isOnline() ? ChatColor.GREEN : ChatColor.DARK_RED) + "⏺" + ChatColor.DARK_GRAY + "]";
            lore.add(reportedByLine);

            SimpleDateFormat submissionDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat submissionTimeFormat = new SimpleDateFormat("HH:mm:ss");
            String dateString = submissionDateFormat.format(submissionDate);
            String timeString = submissionTimeFormat.format(submissionDate);
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

            if(!reportActions.isEmpty()) {
                lore.add("");
                for(ClickType reportActionClickType : reportActions.keySet()) {
                    ReportAction reportAction = reportActions.get(reportActionClickType);
                    lore.add(ChatColor.BLUE + reportAction.getMessage());
                }
            }

            if(resolved) {
                lore.add("");
                lore.add(ChatColor.GREEN + "✓ Resolved");
            }

            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    public void addReportAction(ClickType type, ReportAction action) {
        setOnClickListener(type, (item, player) -> action.invoke((ReportItem) item, player));
        reportActions.put(type, action);
    }

    public void resolve(Player player, boolean resolved) {
        this.resolved = resolved;

        ReportService reportService = ServiceContainer.get(ReportService.class);
        reportService.resolve(player, id, resolved, this::notifyRefresh);

        cachedItemStack = buildInitial(player);
        notifyUpdate();
    }

    protected void addAdditionalArgument(String key, String value) {
        additionalAttributes.put(key, value);
    }
}
