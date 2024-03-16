package com.orangecheese.reports.utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class PluginUpdateChecker {
    private final JavaPlugin plugin;

    private final int resourceId;

    private static final String UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=";

    public PluginUpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void requestVersion(final BiConsumer<String,Boolean> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            String requestUrl = UPDATE_URL + this.resourceId + "/~";
            try (InputStream is = new URL(requestUrl).openStream(); Scanner scanner = new Scanner(is)) {
                if (scanner.hasNext()) {
                    String version = scanner.next();

                    String pluginVersion = plugin.getDescription().getVersion();
                    boolean isOutdated = !getLatestVersion(pluginVersion, version).equals(pluginVersion);

                    consumer.accept(version, isOutdated);
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    private String getLatestVersion(String a, String b) {
        String[] aParts = a.split("\\.");
        String[] bParts = b.split("\\.");

        for (int i = 0; i < aParts.length; i++) {
            int aChunk = Integer.parseInt(aParts[i]);
            int bChunk = Integer.parseInt(bParts[i]);

            if (aChunk > bChunk) {
                return a;
            } else if (aChunk < bChunk) {
                return b;
            }
        }

        return b;
    }
}