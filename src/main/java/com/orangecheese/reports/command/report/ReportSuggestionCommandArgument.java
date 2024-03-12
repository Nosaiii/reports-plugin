package com.orangecheese.reports.command.report;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.CreateSuggestionReportRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import com.orangecheese.reports.service.chatprompt.ChatPromptArgument;
import com.orangecheese.reports.service.chatprompt.ChatPromptCondition;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReportSuggestionCommandArgument implements ICommandArgument {
    private final ContainerCache containerCache;

    private final APIManager apiManager;

    public ReportSuggestionCommandArgument() {
        this.containerCache = ServiceContainer.get(ContainerCache.class);
        this.apiManager = ServiceContainer.get(APIManager.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        ChatPrompt prompt = new ChatPrompt(player, promptArguments -> {
            String message = promptArguments[0];
            boolean anonymous = promptArguments[1].equalsIgnoreCase("yes");

            String accessToken = containerCache.getAccessToken();

            CreateSuggestionReportRequest reportRequest = new CreateSuggestionReportRequest(
                    accessToken,
                    player,
                    message,
                    anonymous,
                    () -> player.sendMessage(ChatColor.GREEN + "A new suggestion report has been submitted!"));

            apiManager.makeRequest(reportRequest);
        });

        prompt.addArgument(new ChatPromptArgument("Describe the suggestion that you would like to submit:", true));

        ChatPromptArgument anonymousChatPromptArgument = new ChatPromptArgument("Would you like to report anonymously? (Yes/No)", true);
        anonymousChatPromptArgument.setCondition(new ChatPromptCondition(
                argument -> argument.matches("(?i)^(yes|no)$"),
                "You have given an invalid yes/no value! Please try again."));
        prompt.addArgument(anonymousChatPromptArgument);

        prompt.start();
    }

    @Override
    public String getBaseCommandArgument() {
        return "suggestion";
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
