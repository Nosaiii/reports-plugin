package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PaginationButton {
    private final Window window;

    private final int pageGoal;

    public static final NamespacedKey KEY = new NamespacedKey(ReportsPlugin.getInstance(), "pagination_button");

    public PaginationButton(Window window, int pageGoal) {
        this.window = window;
        this.pageGoal = pageGoal;
    }

    public ItemStack buildItem(String key) {
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta meta = arrow.getItemMeta();

        if(meta != null) {
            int currentPage = window.getPage();

            String displayName = "";
            if(currentPage + 1 == pageGoal)
                displayName = "Next page →";
            else if(currentPage - 1 == pageGoal)
                displayName = "← Previous page";
            else if(pageGoal > currentPage)
                displayName = String.format("Go to page %s →", pageGoal);
            else if(pageGoal < currentPage)
                displayName = String.format("← Go to page %s", pageGoal);

            meta.setDisplayName(ChatColor.GOLD + displayName);

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(KEY, PersistentDataType.STRING, key);
        }

        arrow.setItemMeta(meta);

        return arrow;
    }

    public void navigate() {
        window.setPage(pageGoal);
    }
}