package com.orangecheese.reports.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class CommandCondition {
    private final BiPredicate<Player, String[]> condition;

    private final Consumer<Player> onCheckFail;

    public CommandCondition(BiPredicate<Player, String[]> condition, Consumer<Player> onCheckFail) {
        this.condition = condition;
        this.onCheckFail = onCheckFail;
    }

    public CommandCondition(BiPredicate<Player, String[]> condition, String message) {
        this(
                condition,
                player -> player.sendMessage(ChatColor.RED + message)
        );
    }

    public boolean test(Player player, String[] arguments) {
        return condition.test(player, arguments);
    }

    public void invokeCheckFail(Player player) {
        onCheckFail.accept(player);
    }
}