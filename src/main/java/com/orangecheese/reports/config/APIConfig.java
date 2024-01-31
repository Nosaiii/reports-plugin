package com.orangecheese.reports.config;

import org.bukkit.configuration.ConfigurationSection;

public class APIConfig {
    private final int version;

    public APIConfig(ConfigurationSection section) {
        this.version = section.getInt("version");
    }

    public int getVersion() {
        return version;
    }
}