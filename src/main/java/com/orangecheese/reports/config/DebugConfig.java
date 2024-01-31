package com.orangecheese.reports.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class DebugConfig {
    private final boolean apiLogOnRequest;

    private final boolean apiShowErrorInConsole;

    public DebugConfig(ConfigurationSection section) {
        ConfigurationSection apiSection = Objects.requireNonNull(section.getConfigurationSection("api"));
        apiLogOnRequest = apiSection.getBoolean("log-on-request");
        apiShowErrorInConsole = apiSection.getBoolean("show-error-in-console");
    }

    public boolean isApiLogOnRequest() {
        return apiLogOnRequest;
    }

    public boolean isApiShowErrorInConsole() {
        return apiShowErrorInConsole;
    }
}