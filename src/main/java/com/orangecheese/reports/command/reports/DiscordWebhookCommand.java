package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.container.ContainerDiscordWebhookRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DiscordWebhookCommand implements ICommandArgument {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    public DiscordWebhookCommand() {
        apiManager = ServiceContainer.get(APIManager.class);
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String url = arguments[1];

        ContainerDiscordWebhookRequest request = new ContainerDiscordWebhookRequest(
                url,
                containerCache.getAccessToken(),
                () -> {
                    player.sendMessage(ChatColor.GREEN + "Successfully set the Discord webhook to the given URL!");
                    player.sendMessage(ChatColor.YELLOW + "The Discord webhook has sent a message in the server indicating the webhook has been bound to this container.");
                },
                messageResponse -> player.sendMessage(
                        ChatColor.RED + "Something went wrong while setting the Discord webhook to this container: " + messageResponse.getMessage()
                )
        );

        apiManager.makeRequest(request);
    }

    @Override
    public String getBaseCommandArgument() {
        return "discordwebhook";
    }

    @Override
    public String getUsage() {
        return "<url>";
    }

    @Override
    public int getRequiredArguments() {
        return 2;
    }

    @Override
    public boolean hasInfiniteArgument() {
        return false;
    }
}
