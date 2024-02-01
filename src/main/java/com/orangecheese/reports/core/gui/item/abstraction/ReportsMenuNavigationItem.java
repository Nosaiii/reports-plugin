package com.orangecheese.reports.core.gui.item.abstraction;

import com.orangecheese.reports.core.gui.item.MenuNavigationItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReportsMenuNavigationItem extends MenuNavigationItem {
    private int reports;

    public ReportsMenuNavigationItem(Window context, Window to, Material material, String label) {
        super(
                context,
                to,
                "",
                material,
                label,
                "A collection of " + StringUtils.uncapitalize(label) + ".",
                "Fetching...");
        reports = -1;
    }

    public void setReports(Player player, int reports) {
        this.reports = reports;
        setAdditionalLore(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + reports + ChatColor.DARK_GRAY + " report(s) «");
        notifyUpdate(player);
    }

    public int getReports() {
        return reports;
    }
}