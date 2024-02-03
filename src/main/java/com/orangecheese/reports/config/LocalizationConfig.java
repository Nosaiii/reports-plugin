package com.orangecheese.reports.config;

import org.bukkit.configuration.ConfigurationSection;

public class LocalizationConfig {
    private final String timezone;

    public LocalizationConfig(ConfigurationSection section) {
        this.timezone = section.getString("timezone", "GMT+1");
    }

    public String getTimezone() {
        return timezone;
    }
}