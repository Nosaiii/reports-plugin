package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.io.ContainerCache;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LinkCommand implements ICommandArgument {
    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String identification = arguments[1];
        String keyPhrase = arguments[2];

        ContainerCache containerCache = ServiceContainer.get(ContainerCache.class);

        containerCache.checkAuthentication(identification, keyPhrase, tokenResponse -> {
            containerCache.setAccessToken(tokenResponse.getToken());
            player.sendMessage(ChatColor.GREEN + "Successfully changed the plugin's container ID to '" + identification + "'!");
        }, message -> player.sendMessage(ChatColor.RED +
                StringUtils.capitalize(message.getMessage() + ".") +
                " " +
                "If you wish to register a new container, please use " + ChatColor.GRAY + "/reports register" + ChatColor.RED + "."
        ));
    }

    @Override
    public String getBaseCommandArgument() {
        return "link";
    }

    @Override
    public String getUsage() {
        return "<identification> <key phrase>";
    }

    @Override
    public int getRequiredArguments() {
        return 3;
    }

    @Override
    public boolean hasInfiniteArgument() {
        return false;
    }
}
