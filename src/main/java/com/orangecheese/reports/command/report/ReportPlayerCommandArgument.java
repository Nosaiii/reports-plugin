package com.orangecheese.reports.command.report;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.CreatePlayerReportRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.PlayerProfileService;
import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import com.orangecheese.reports.service.chatprompt.ChatPromptArgument;
import com.orangecheese.reports.service.chatprompt.ChatPromptCondition;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportPlayerCommandArgument implements ICommandArgument {
    private final ContainerCache containerCache;

    private final APIManager apiManager;

    private final PlayerProfileService playerProfileService;

    public ReportPlayerCommandArgument() {
        containerCache = ServiceContainer.get(ContainerCache.class);
        apiManager = ServiceContainer.get(APIManager.class);
        playerProfileService = ServiceContainer.get(PlayerProfileService.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        ChatPrompt prompt = new ChatPrompt(player, promptArguments -> {
            String playerName = promptArguments[0];
            String message = promptArguments[1];
            boolean anonymous = promptArguments[2].equalsIgnoreCase("yes");

            playerProfileService.getWithCatch(
                    playerName,
                    profile -> {
                        UUID playerUuid = profile.getUniqueId();

                        String accessToken = containerCache.getAccessToken();

                        CreatePlayerReportRequest reportRequest = new CreatePlayerReportRequest(
                                accessToken,
                                player,
                                message,
                                anonymous,
                                playerUuid,
                                () -> player.sendMessage(ChatColor.GREEN + "A new player report has been submitted!"));

                        apiManager.makeRequest(reportRequest);
                    },
                    response -> {
                        player.sendMessage(ChatColor.RED + "Something went wrong: " + response.getMessage() + ".");
                        player.sendMessage(ChatColor.RED + "Please try again in a minute.");
                    });
        });

        ChatPromptArgument playerArgument = new ChatPromptArgument("What player would you like to report?");
        ChatPromptArgument messageArgument = new ChatPromptArgument("What do you want to report the player about?");
        ChatPromptArgument anonymousChatPromptArgument = new ChatPromptArgument("Would you like to report anonymously? (Yes/No)");
        anonymousChatPromptArgument.setCondition(new ChatPromptCondition(
                argument -> argument.matches("(?i)^(yes|no)$"),
                "You have given an invalid yes/no value! Please try again."));

        prompt.addArgument(playerArgument);
        prompt.addArgument(messageArgument);
        prompt.addArgument(anonymousChatPromptArgument);

        prompt.start();
    }

    @Override
    public String getBaseCommandArgument() {
        return "player";
    }

    @Override
    public String getUsage() {
        return "» You will be prompted to enter details of the report";
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
