package com.orangecheese.reports.core.gui.item.abstraction;

import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class WindowItem {
    private final Window context;

    private final Map<ClickType, BiConsumer<WindowItem, Player>> onClick;

    protected ItemStack cachedItemStack;

    private String[] requiredPermissions;

    public WindowItem(Window context) {
        this.context = context;
        onClick = new HashMap<>();
        cachedItemStack = new ItemStack(Material.BARRIER);
    }

    public void setOnClickListener(ClickType type, BiConsumer<WindowItem, Player> onClick) {
        this.onClick.put(type, onClick);
    }

    public void onClick(ClickType type, Player player) {
        if(!onClick.containsKey(type))
            return;
        onClick.get(type).accept(this, player);
    }

    public void setRequiredPermissions(String... requiredPermissions) {
        requiredPermissions = Arrays.stream(requiredPermissions)
                .filter(permission -> permission != null && !permission.isEmpty())
                .toArray(String[]::new);

        this.requiredPermissions = requiredPermissions;
    }

    public boolean hasPermissions(Player player) {
        if(requiredPermissions.length == 0)
            return true;

        return Arrays.stream(requiredPermissions).allMatch(player::hasPermission);
    }

    public ItemStack buildInitial(Player player) {
        ItemStack initialItemStack = renderInitial(player);
        cachedItemStack = initialItemStack;
        return initialItemStack;
    }

    public void notifyUpdate(Player player) {
        cachedItemStack = buildInitial(player);
        context.notifyUpdate(this);
    }

    public void notifyRefresh() {
        context.refresh(context.getPage());
    }

    protected abstract ItemStack renderInitial(Player player);

    public ItemStack getItemStack() {
        return cachedItemStack;
    }
}