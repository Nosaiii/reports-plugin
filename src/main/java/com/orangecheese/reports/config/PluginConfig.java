package com.orangecheese.reports.config;

import org.bukkit.configuration.ConfigurationSection;

public class PluginConfig {
    private final boolean useUpdateChecker;

    public PluginConfig(ConfigurationSection section) {
        useUpdateChecker = section.getBoolean("use-update-checker");
    }

    public boolean isUseUpdateChecker() {
        return useUpdateChecker;
    }
}