package com.orangecheese.reports.command.report;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.CreateBugReportRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.chatprompt.ChatPrompt;
import com.orangecheese.reports.service.chatprompt.ChatPromptArgument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReportBugCommandArgument implements ICommandArgument {
    private final ContainerCache containerCache;

    private final APIManager apiManager;

    public ReportBugCommandArgument() {
        this.containerCache = ServiceContainer.get(ContainerCache.class);
        this.apiManager = ServiceContainer.get(APIManager.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        ChatPrompt prompt = new ChatPrompt(player, promptArguments -> {
            String message = promptArguments[0];
            String stepsToReproduce = promptArguments[1];

            String accessToken = containerCache.getAccessToken();

            CreateBugReportRequest reportRequest = new CreateBugReportRequest(
                    accessToken,
                    player,
                    message,
                    stepsToReproduce,
                    () -> player.sendMessage(ChatColor.GREEN + "A new bug report has been submitted!"));

            apiManager.makeRequest(reportRequest);
        });

        prompt.addArgument(new ChatPromptArgument("Describe the bug that you had encountered:"));
        prompt.addArgument(new ChatPromptArgument("Describe the steps to reproduce the bug:"));

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
