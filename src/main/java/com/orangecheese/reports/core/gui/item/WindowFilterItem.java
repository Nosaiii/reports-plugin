package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WindowFilterItem extends WindowItem {
    private final String label;

    private boolean active;

    public WindowFilterItem(Window context, String label) {
        super(context);
        this.label = label;
        active = true;

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            WindowFilterItem windowFilterItem = (WindowFilterItem) item;
            windowFilterItem.setActive(player, !windowFilterItem.active);
        });
    }

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(active ? Material.LIME_DYE : Material.GRAY_DYE);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(ChatColor.DARK_GRAY + "Filter: " + ChatColor.GOLD + label);

            String activeStatus;
            if(active)
                activeStatus = ChatColor.GREEN + "" + ChatColor.BOLD + "ON";
            else
                activeStatus = ChatColor.RED + "" + ChatColor.BOLD + "OFF";
            meta.setLore(List.of(activeStatus));
        }

        item.setItemMeta(meta);

        return item;
    }

    public void setActive(Player player, boolean active) {
        this.active = active;
        cachedItemStack = renderInitial(player);
        notifyRefresh();
    }

    public boolean isActive() {
        return active;
    }
}