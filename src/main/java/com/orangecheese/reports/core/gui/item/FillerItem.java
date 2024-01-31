package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FillerItem extends WindowItem {
    public FillerItem(Window context) {
        super(context);
    }

    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
        }

        return item;
    }
}