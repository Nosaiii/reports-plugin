package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.container.ContainerDeleteCurrentRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import com.orangecheese.reports.service.chatprompt.ChatPromptArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeleteCurrentCommand implements ICommandArgument {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    public DeleteCurrentCommand() {
        apiManager = ServiceContainer.get(APIManager.class);
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        ChatPrompt prompt = new ChatPrompt(player, promptArguments -> {
            boolean confirmed = promptArguments[0].equalsIgnoreCase("confirm");

            if(!confirmed) {
                player.sendMessage(ChatColor.RED + "The deletion has been cancelled due to incorrectly submitting the confirmation prompt.");
                return;
            }

            ContainerDeleteCurrentRequest deleteRequest = new ContainerDeleteCurrentRequest(
                    containerCache.getAccessToken(),
                    () -> {
                        containerCache.unset();
                        player.sendMessage(ChatColor.GREEN + "You have successfully removed the container!");
                    },
                    messageResponse -> player.sendMessage(ChatColor.RED + "Something went wrong trying to delete the current container: " + messageResponse.getMessage()));

            apiManager.makeRequest(deleteRequest);
        });

        ChatPromptArgument confirmArgument = new ChatPromptArgument("Are you sure you want to delete the current container? " +
                "Any reports associated to the container will also be deleted. " +
                "Type 'confirm' to verify the deletion of the container.");
        prompt.addArgument(confirmArgument);

        prompt.start();
    }

    @Override
    public String getBaseCommandArgument() {
        return "deletecurrent";
    }

    @Override
    public String getUsage() {
        return "";
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
