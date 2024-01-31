package com.orangecheese.reports.core.gui.data;

import org.bukkit.entity.Player;

import java.util.Stack;

public class WindowHistory {
    private final Stack<WindowHistoryEntry> stack;

    public WindowHistory() {
        stack = new Stack<>();
    }

    public void add(WindowHistoryEntry entry) {
        stack.add(entry);
    }

    public void navigateBack(Player player) {
        stack.pop();

        if(stack.isEmpty()) {
            player.closeInventory();
            return;
        }

        WindowHistoryEntry entry = stack.pop();
        entry.getWindow().open(entry.getPage());
    }
}