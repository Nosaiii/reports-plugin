package com.orangecheese.reports.utility;

import com.orangecheese.reports.ReportsPlugin;
import org.bukkit.Bukkit;

public final class ThreadUtility {
    public static void executeOnMainThread(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(ReportsPlugin.getInstance(), runnable);
    }
}