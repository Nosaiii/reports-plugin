package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.command.BaseCommand;

public class ReportsCommand extends BaseCommand {
    public ReportsCommand() {
        super("reports", "reports.reports");

        setBaseCommandArgument(new ReportsBaseCommand());

        addArgument("help", new HelpCommand());
        addArgument("register", new RegisterCommand());
        addArgument("link", new LinkCommand());
        addArgument("changekeyphrase", new ChangeKeyPhraseCommand());
        addArgument("chathistory", new ChatHistoryCommand());
        addArgument("reload", new ReloadCommand());
        addArgument("delete", new DeleteCommand());
        addArgument("deletecurrent", new DeleteCurrentCommand());
    }
}
