package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.service.ReportService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ResolveReportItem extends WindowItem {
    private boolean resolve;

    private final ReportService reportService;

    public ResolveReportItem(Window context, int id, boolean resolve) {
        super(context);
        this.resolve = resolve;

        reportService = ServiceContainer.get(ReportService.class);

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            reportService.resolve(player, id, resolve, () -> {
                this.resolve = !resolve;
                notifyUpdate(player);
            });
        });
    }

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(resolve ? Material.LIME_STAINED_GLASS : Material.RED_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            String displayName = resolve ?
                    ChatColor.GREEN + "Resolve" :
                    ChatColor.RED + "Unresolve";
            meta.setDisplayName(displayName);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + (resolve ? "Resolve" : "Unresolve") + " the report.");
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}
