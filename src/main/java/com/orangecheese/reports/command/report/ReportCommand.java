package com.orangecheese.reports.command.report;

import com.orangecheese.reports.command.BaseCommand;

public class ReportCommand extends BaseCommand {
    public ReportCommand() {
        super("report", "reports.report");

        addArgument("bug", new ReportBugCommandArgument());
        addArgument("player", new ReportPlayerCommandArgument());
        addArgument("suggestion", new ReportSuggestionCommandArgument());
    }
}
