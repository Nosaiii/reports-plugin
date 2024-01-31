package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import org.bukkit.entity.Player;

public class HelpCommand implements ICommandArgument {
    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        baseCommand.sendUsageMessage(player, null);
    }

    @Override
    public String getBaseCommandArgument() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "» You will be shown this help page";
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
