package com.orangecheese.reports.core.gui.item.abstraction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IAutoUpdatingWindowItem {
    ItemStack update(Player player);
}