package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.container.ContainerChangeKeyPhraseRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChangeKeyPhraseCommand implements ICommandArgument {
    private final ContainerCache containerCache;

    public ChangeKeyPhraseCommand() {
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String identification = arguments[1];
        String currentKeyPhrase = arguments[2];
        String newKeyPhrase = arguments[3];

        APIManager apiManager = ServiceContainer.get(APIManager.class);
        ContainerChangeKeyPhraseRequest registerRequest = new ContainerChangeKeyPhraseRequest(
                identification,
                currentKeyPhrase,
                newKeyPhrase,
                response -> {
                    containerCache.setAccessToken(response.getToken());
                    player.sendMessage(ChatColor.GREEN + "Successfully changed the key phrase of the container with ID '" + identification + "'!");
                    player.sendMessage(ChatColor.YELLOW + "Note that you will have to relink servers (other than this one) that were linked to this container before.");
                },
                failure -> player.sendMessage(ChatColor.RED + StringUtils.capitalize(failure.getMessage() + "!")));

        apiManager.makeRequest(registerRequest);
    }

    @Override
    public String getBaseCommandArgument() {
        return "changekeyphrase";
    }

    @Override
    public String getUsage() {
        return "<identification> <current key phrase> <new key phrase>";
    }

    @Override
    public int getRequiredArguments() {
        return 4;
    }

    @Override
    public boolean hasInfiniteArgument() {
        return false;
    }
}
