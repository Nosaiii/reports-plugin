package com.orangecheese.reports.command;

import org.bukkit.entity.Player;

public interface ICommandArgument {
    void execute(Player player, String[] arguments, BaseCommand baseCommand);

    String getBaseCommandArgument();

    String getUsage();

    int getRequiredArguments();

    boolean hasInfiniteArgument();
}