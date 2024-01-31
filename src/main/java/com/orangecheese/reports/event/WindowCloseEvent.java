package com.orangecheese.reports.event;

import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class WindowCloseEvent implements Listener {
    private final Window window;

    public WindowCloseEvent(Window window) {
        this.window = window;
    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if(inventory != window.getInventory())
            return;

        window.dispose();
    }
}