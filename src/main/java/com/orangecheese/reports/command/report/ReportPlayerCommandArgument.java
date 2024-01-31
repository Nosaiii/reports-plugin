package com.orangecheese.reports.command.report;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.CreatePlayerReportRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import com.orangecheese.reports.service.chatprompt.ChatPromptArgument;
import com.orangecheese.reports.service.chatprompt.ChatPromptCondition;
import com.orangecheese.reports.service.chatprompt.ChatPromptPlaceholder;
import com.orangecheese.reports.utility.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.util.UUID;

public class ReportPlayerCommandArgument implements ICommandArgument {
    private final ContainerCache containerCache;

    private final APIManager apiManager;

    public ReportPlayerCommandArgument() {
        this.containerCache = ServiceContainer.get(ContainerCache.class);
        this.apiManager = ServiceContainer.get(APIManager.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        ChatPrompt prompt = new ChatPrompt(player, promptArguments -> {
            String playerName = promptArguments[0];
            String message = promptArguments[1];

            PlayerProfile playerProfile = PlayerUtility.getProfile(playerName);
            if(playerProfile == null)
                throw new RuntimeException("player is null: " + playerName);
            UUID playerUuid = playerProfile.getUniqueId();

            String accessToken = containerCache.getAccessToken();

            CreatePlayerReportRequest reportRequest = new CreatePlayerReportRequest(
                    accessToken,
                    player,
                    message,
                    playerUuid,
                    () -> player.sendMessage(ChatColor.GREEN + "A new player report has been submitted!"));

            apiManager.makeRequest(reportRequest);
        });

        ChatPromptArgument playerArgument = new ChatPromptArgument("What player would you like to report?");
        playerArgument.setPlaceholder(new ChatPromptPlaceholder("player", name -> {
            PlayerProfile profile = PlayerUtility.getProfile(name);
            if(profile == null)
                return name;
            return profile.getName();
        }));
        playerArgument.setCondition(new ChatPromptCondition(name -> {
            PlayerProfile profile = PlayerUtility.getProfile(name);
            return profile != null;
        }, "The player '%player%' is invalid! Please try again."));

        ChatPromptArgument messageArgument = new ChatPromptArgument("What do you want to report %player% about?");

        prompt.addArgument(playerArgument);
        prompt.addArgument(messageArgument);

        prompt.start();
    }

    @Override
    public String getBaseCommandArgument() {
        return "bug";
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
