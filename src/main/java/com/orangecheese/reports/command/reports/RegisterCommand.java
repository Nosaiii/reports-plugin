package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.container.ContainerRegisterRequest;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RegisterCommand implements ICommandArgument {
    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String identification = arguments[1];
        String keyPhrase = arguments[2];
        UUID uuid = player.getUniqueId();

        APIManager apiManager = ServiceContainer.get(APIManager.class);
        ContainerRegisterRequest registerRequest = new ContainerRegisterRequest(
                identification,
                keyPhrase,
                uuid,
                () -> player.sendMessage(ChatColor.GREEN + "Successfully registered a new container with ID '" + identification + "'!"),
                failure -> player.sendMessage(ChatColor.RED + StringUtils.capitalize(failure.getMessage() + "!")));

        apiManager.makeRequest(registerRequest);
    }

    @Override
    public String getBaseCommandArgument() {
        return "register";
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
