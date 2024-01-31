package com.orangecheese.reports.command;

import org.bukkit.entity.Player;

public interface IBaseCommandArgument {
    boolean execute(Player player, String[] arguments);

    boolean hasInfiniteArgument();
}