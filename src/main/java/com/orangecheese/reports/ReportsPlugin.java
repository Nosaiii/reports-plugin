package com.orangecheese.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.report.ReportCommand;
import com.orangecheese.reports.command.reports.ReportsCommand;
import com.orangecheese.reports.config.ReportsConfig;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ReportsPlugin extends JavaPlugin {
    private static ReportsPlugin instance;

    private ReportsConfig reportsConfig;

    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadReportsConfig();

        ServiceContainer.bind(APIManager.class);
        ServiceContainer.bind(ContainerCache.class);
        ServiceContainer.bind(ReportService.class);

        if(!initializeAPI())
            return;

        registerCommand("reports", new ReportsCommand());
        registerCommand("report", new ReportCommand());
    }

    private boolean initializeAPI() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        APIManager apiManager = ServiceContainer.get(APIManager.class);

        apiManager.setFailedConnectionHandler(() -> {
            sender.sendMessage(ChatColor.RED + "Failed to establish a connection with the API. Please contact the developer if this issue continues to occur.");
            disablePlugin();
        });

        String pingAddress = apiManager.getHostName();
        if(apiManager.getPort() != 80)
            pingAddress += ":" + apiManager.getPort();
        sender.sendMessage(ChatColor.WHITE + "Pinging the API at '" + pingAddress + "'...");

        boolean isAlive = apiManager.ping();
        if(isAlive) {
            sender.sendMessage(ChatColor.GREEN + "SUCCESS! " + ChatColor.WHITE + "The API is up and healthy!");
        } else {
            sender.sendMessage(ChatColor.RED + "ERROR! " + ChatColor.WHITE + "The ping to the API failed and no connection could be established with the API.");
            disablePlugin();
            return false;
        }

        return true;
    }

    private void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(command)).setExecutor(executor);
    }

    private void disablePlugin() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "The plugin will be disabled. Reload the server to try again.");
        Bukkit.getServer().getPluginManager().disablePlugin(this);
    }

    public void reloadReportsConfig() {
        reloadConfig();
        reportsConfig = new ReportsConfig();
    }

    public ReportsConfig getReportsConfig() {
        return reportsConfig;
    }

    public static ReportsPlugin getInstance() {
        return instance;
    }
}