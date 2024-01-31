package com.orangecheese.reports.config;

import com.orangecheese.reports.core.http.HTTPProtocol;
import org.bukkit.configuration.ConfigurationSection;

public class APIConfig {
    private final HTTPProtocol protocol;

    private final String hostName;

    private final int port;

    private final int version;

    public APIConfig(ConfigurationSection section) {
        this.protocol = HTTPProtocol.valueOf(section.getString("protocol", "HTTPS").toUpperCase());
        this.hostName = section.getString("host-name", "reportsplugin.com");
        this.port = section.getInt("port", 443);
        this.version = section.getInt("version", 1);
    }

    public HTTPProtocol getProtocol() {
        return protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public int getVersion() {
        return version;
    }
}