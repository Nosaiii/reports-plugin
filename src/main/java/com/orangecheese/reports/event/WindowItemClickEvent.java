package com.orangecheese.reports.event;

import com.orangecheese.reports.core.gui.item.PaginationButton;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WindowItemClickEvent implements Listener {
    private final Window window;

    public WindowItemClickEvent(Window window) {
        this.window = window;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if(inventory != window.getInventory())
            return;

        if(!(event.getWhoClicked() instanceof Player player))
            return;

        event.setCancelled(true);

        if(event.getClick() == ClickType.RIGHT) {
            window.navigateBack();
            return;
        }

        if(event.getCurrentItem() != null &&
                event.getCurrentItem().hasItemMeta() &&
                event.getCurrentItem().getItemMeta() != null) {
            PersistentDataContainer dataContainer = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
            if(dataContainer.has(PaginationButton.KEY)) {
                String paginationButtonKey = dataContainer.get(PaginationButton.KEY, PersistentDataType.STRING);
                PaginationButton paginationButton = window.getPaginationButton(paginationButtonKey);
                if(paginationButton != null)
                    paginationButton.navigate();
                return;
            }
        }

        int clickedSlot = event.getSlot();
        if(!window.hasSlottedItem(clickedSlot))
            return;

        WindowItem clickedItem = window.getItem(clickedSlot);
        clickedItem.onClick(event.getClick(), player);
    }
}