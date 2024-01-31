package com.orangecheese.reports.core.gui.data;

import com.orangecheese.reports.core.gui.window.abstraction.Window;

public class WindowHistoryEntry {
    private final Window window;

    private final int page;

    public WindowHistoryEntry(Window window, int page) {
        this.window = window;
        this.page = page;
    }

    public WindowHistoryEntry(Window window) {
        this(window, 1);
    }

    public Window getWindow() {
        return window;
    }

    public int getPage() {
        return page;
    }
}