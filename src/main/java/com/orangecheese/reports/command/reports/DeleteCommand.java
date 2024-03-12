package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.container.ContainerDeleteRequest;
import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import com.orangecheese.reports.service.chatprompt.ChatPromptArgument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeleteCommand implements ICommandArgument {
    private final APIManager apiManager;

    public DeleteCommand() {
        apiManager = ServiceContainer.get(APIManager.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String identification = arguments[1];
        String keyPhrase = arguments[2];

        ChatPrompt prompt = new ChatPrompt(player, promptArguments -> {
            boolean confirmed = promptArguments[0].equalsIgnoreCase("confirm");

            if(!confirmed) {
                player.sendMessage(ChatColor.RED + "The deletion has been cancelled due to incorrectly submitting the confirmation prompt.");
                return;
            }

            ContainerDeleteRequest deleteRequest = new ContainerDeleteRequest(
                    identification,
                    keyPhrase,
                    () -> player.sendMessage(ChatColor.GREEN + "You have successfully removed the container!"),
                    messageResponse -> player.sendMessage(ChatColor.RED + "Something went wrong trying to delete the container: " + messageResponse.getMessage()));

            apiManager.makeRequest(deleteRequest);
        });

        ChatPromptArgument confirmArgument = new ChatPromptArgument("Are you sure you want to delete the container? " +
                "Any reports associated to the container will also be deleted. " +
                "Type 'confirm' to verify the deletion of the container.",
                true);
        prompt.addArgument(confirmArgument);

        prompt.start();
    }

    @Override
    public String getBaseCommandArgument() {
        return "delete";
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
