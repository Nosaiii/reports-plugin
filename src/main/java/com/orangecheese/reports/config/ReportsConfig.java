package com.orangecheese.reports.config;

import com.orangecheese.reports.ReportsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;

public class ReportsConfig {
    private final FileConfiguration configuration;

    private final APIConfig apiConfig;

    private final MenuConfig menuConfig;

    private final PluginConfig pluginConfig;

    private final DebugConfig debugConfig;

    public ReportsConfig() {
        this.configuration = ReportsPlugin.getInstance().getConfig();

        apiConfig = new APIConfig(getConfigurationSection("api"));
        menuConfig = new MenuConfig(getConfigurationSection("menu"));
        pluginConfig = new PluginConfig(getConfigurationSection("plugin"));
        debugConfig = new DebugConfig(getConfigurationSection("debug"));
    }

    public APIConfig getApi() {
        return apiConfig;
    }

    public MenuConfig getMenu() {
        return menuConfig;
    }

    public PluginConfig getPlugin() {
        return pluginConfig;
    }

    public DebugConfig getDebug() {
        return debugConfig;
    }

    private ConfigurationSection getConfigurationSection(@Nonnull String path) {
        return configuration.getConfigurationSection(path);
    }
}