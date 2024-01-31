package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.BugReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.ReportsWindow;
import com.orangecheese.reports.core.http.request.report.ReadBugReportsRequest;
import com.orangecheese.reports.core.http.response.BugReportAttributes;
import com.orangecheese.reports.core.http.response.ReportResponse;
import org.bukkit.entity.Player;

public class BugReportsWindow extends ReportsWindow {
    public BugReportsWindow(Player player, WindowHistory history) {
        super(player, "Bug reports", history);
    }

    @Override
    public void onInitialize(int page) {
        super.onInitialize(page);

        boolean showResolved = resolvedFilter.isActive();
        boolean showUnresolved = unresolvedFilter.isActive();

        ReadBugReportsRequest reportsRequest = new ReadBugReportsRequest(
                page,
                showResolved,
                showUnresolved,
                response -> {
                    for(ReportResponse<BugReportAttributes> report : response.getReports()) {
                        BugReportItem item = new BugReportItem(
                                this,
                                report.getId(),
                                report.getReporterUuid(),
                                report.getMessage(),
                                report.isResolved(),
                                report.getCreatedAt(),
                                report.getAttributes().getStepsToReproduce()
                        );
                        addItem(item);
                    }
                });
        apiManager.makeRequest(reportsRequest);
    }
}
