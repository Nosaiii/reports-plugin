package com.orangecheese.reports.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MenuConfig {
    private final ChatColor titleColor;

    private final Map<String, Material> mainIcons;

    private final boolean showPageNumber;

    private final boolean useFilters;

    public MenuConfig(ConfigurationSection section) {
        titleColor = ChatColor.getByChar(Objects.requireNonNull(section.getString("title-color")));

        ConfigurationSection mainSection = Objects.requireNonNull(section.getConfigurationSection("main"));

        ConfigurationSection iconsSection = Objects.requireNonNull(mainSection.getConfigurationSection("icons"));
        mainIcons = new HashMap<>();
        for(String iconKey : iconsSection.getKeys(false)) {
            String materialName = Objects.requireNonNull(iconsSection.getString(iconKey));
            Material material = Material.matchMaterial(materialName);
            if(material != null) {
                mainIcons.put(iconKey, material);
            }
        }

        ConfigurationSection reportsSection = Objects.requireNonNull(section.getConfigurationSection("reports"));
        showPageNumber = reportsSection.getBoolean("show-page-number");
        useFilters = reportsSection.getBoolean("use-filters");
    }

    public ChatColor getTitleColor() {
        return titleColor;
    }

    public Material getMainIcon(String key) {
        if(!mainIcons.containsKey(key))
            throw new NullPointerException("key for icon does not exist");
        return mainIcons.get(key);
    }

    public boolean isShowPageNumber() {
        return showPageNumber;
    }

    public boolean isUseFilters() {
        return useFilters;
    }
}