package com.orangecheese.reports.core.gui.item.abstraction;

import com.orangecheese.reports.core.gui.item.MenuNavigationItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ReportsMenuNavigationItem extends MenuNavigationItem implements IAutoUpdatingWindowItem {
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

    public void setReports(int reports) {
        this.reports = reports;
    }

    @Override
    public ItemStack update(Player player) {
        ItemMeta meta = cachedItemStack.getItemMeta();

        if(meta != null && meta.hasLore() && meta.getLore() != null) {
            if(reports == -1)
                setAdditionalLore(ChatColor.DARK_GRAY + "Fetching...");
            else
                setAdditionalLore(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + reports + ChatColor.DARK_GRAY + " report(s) «");
            List<String> lore = getLore(player);
            meta.setLore(lore);
        }

        cachedItemStack.setItemMeta(meta);

        return cachedItemStack;
    }
}