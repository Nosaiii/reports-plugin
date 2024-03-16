package com.orangecheese.reports.command;

import com.orangecheese.reports.utility.CaseInsensitiveLinkedHashMap;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseCommand implements CommandExecutor {
    private final String command;

    private final String permission;

    private final CaseInsensitiveLinkedHashMap<ICommandArgument> arguments;

    private final ArrayList<CommandCondition> conditions;

    private IBaseCommandArgument baseCommandArgument;

    public BaseCommand(String command, String permission) {
        this.command = command;
        this.permission = permission;
        arguments = new CaseInsensitiveLinkedHashMap<>();
        conditions = new ArrayList<>();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(!cmd.getName().equalsIgnoreCase(command))
            return false;

        if(!(sender instanceof Player player)) {
            if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ChatColor.RED + "This command is only executable as an in-game player!");
            }
            return false;
        }

        if(!hasPermission(player)) {
            player.sendMessage(ChatColor.RED + "You are not permitted to perform this command!");
            return false;
        }

        for(CommandCondition condition : conditions) {
            if(!condition.test(player, args)) {
                condition.invokeCheckFail(player);
                return false;
            }
        }

        if(args.length == 0) {
            if(baseCommandArgument != null) {
                return baseCommandArgument.execute(player, args);
            }
            sendUsageMessage(player, "Too few arguments!");
            return false;
        }

        if(baseCommandArgument != null && baseCommandArgument.hasInfiniteArgument()) {
            return baseCommandArgument.execute(player, args);
        }

        String argument = args[0];
        if(!arguments.containsKey(argument)) {
            sendUsageMessage(player, "Incorrect usage of argument '" + argument + "'!");
            return false;
        }

        ICommandArgument commandArgument = arguments.get(argument);

        if(args.length < commandArgument.getRequiredArguments()) {
            sendArgumentUsageMessage(player, commandArgument, "Too few arguments!");
            return false;
        }

        if(!commandArgument.hasInfiniteArgument() && args.length > commandArgument.getRequiredArguments()) {
            sendArgumentUsageMessage(player, commandArgument, "Too many arguments!");
            return false;
        }

        commandArgument.execute(player, args, this);

        return true;
    }

    public String getCommand() {
        return command;
    }

    protected void addArgument(String argumentName, ICommandArgument argument) {
        arguments.put(argumentName, argument);
    }

    protected void addCondition(CommandCondition condition) {
        conditions.add(condition);
    }

    protected void setBaseCommandArgument(IBaseCommandArgument argument) {
        baseCommandArgument = argument;
    }

    public void sendUsageMessage(Player player, String reason) {
        if(reason != null)
            player.sendMessage(ChatColor.DARK_RED + reason);

        player.sendMessage(ChatColor.RED + "Available arguments:");

        for(String usageStringKey : getUsageStringKeys()) {
            String usageString = arguments.get(usageStringKey).getUsage();
            String baseCommand = StringUtils.capitalize(usageStringKey);
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + ChatColor.BOLD + baseCommand + " " + ChatColor.GRAY + usageString);
        }
    }

    private void sendArgumentUsageMessage(Player player, ICommandArgument argument, String reason) {
        String baseCommand = StringUtils.uncapitalize(argument.getBaseCommandArgument());
        String usage = argument.getUsage();

        player.sendMessage(ChatColor.DARK_RED + reason);
        player.sendMessage(ChatColor.GRAY + "Usage: /" + command + " " + ChatColor.RED + ChatColor.BOLD + baseCommand + " " + ChatColor.GRAY + usage);
    }

    private Set<String> getUsageStringKeys() {
        return arguments
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> arguments.get(key).getUsage())
                ).keySet();
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission(permission) || player.isOp();
    }
}