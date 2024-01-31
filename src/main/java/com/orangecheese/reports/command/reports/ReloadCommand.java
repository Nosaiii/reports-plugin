package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCommand implements ICommandArgument {
    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        ReportsPlugin.getInstance().reloadReportsConfig();
        player.sendMessage(ChatColor.GREEN + "The plugin has been reloaded!");
    }

    @Override
    public String getBaseCommandArgument() {
        return "reload";
    }

    @Override
    public String getUsage() {
        return "» Reloads the plugin (and config)";
    }

    @Override
    public int getRequiredArguments() {
        return 1;
    }

    @Override
    public boolean hasInfiniteArgument() {
        return false;
    }
}
