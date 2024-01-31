package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.SuggestionReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.ReportsWindow;
import com.orangecheese.reports.core.http.request.report.ReadSuggestionReportsRequest;
import com.orangecheese.reports.core.http.response.ReportResponse;
import com.orangecheese.reports.core.http.response.SuggestionReportAttributes;
import org.bukkit.entity.Player;

public class SuggestionReportsWindow extends ReportsWindow {
    public SuggestionReportsWindow(Player player, WindowHistory history) {
        super(player, "Suggestions", history);
    }

    @Override
    public void onInitialize(int page) {
        super.onInitialize(page);

        boolean showResolved = resolvedFilter.isActive();
        boolean showUnresolved = unresolvedFilter.isActive();

        ReadSuggestionReportsRequest request = new ReadSuggestionReportsRequest(
                page,
                showResolved,
                showUnresolved,
                response -> {
                    for(ReportResponse<SuggestionReportAttributes> report : response.getReports()) {
                        SuggestionReportItem item = new SuggestionReportItem(
                                this,
                                report.getId(),
                                report.getReporterUuid(),
                                report.getMessage(),
                                report.isResolved(),
                                report.getCreatedAt()
                        );
                        addItem(item);
                    }
                }
        );
        apiManager.makeRequest(request);
    }
}
