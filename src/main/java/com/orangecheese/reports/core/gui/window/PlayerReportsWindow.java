package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.PlayerReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.ReportsWindow;
import com.orangecheese.reports.core.http.request.report.ReadPlayerReportsRequest;
import com.orangecheese.reports.core.http.response.PlayerReportAttributes;
import com.orangecheese.reports.core.http.response.ReportResponse;
import org.bukkit.entity.Player;

public class PlayerReportsWindow extends ReportsWindow {
    public PlayerReportsWindow(Player player, WindowHistory history) {
        super(player, "Player reports", history);
    }

    @Override
    public void onInitialize(int page) {
        super.onInitialize(page);

        boolean showResolved = resolvedFilter.isActive();
        boolean showUnresolved = unresolvedFilter.isActive();

        ReadPlayerReportsRequest request = new ReadPlayerReportsRequest(
                page,
                showResolved,
                showUnresolved,
                response -> {
                    for(ReportResponse<PlayerReportAttributes> report : response.getReports()) {
                        PlayerReportItem item = new PlayerReportItem(
                                this,
                                report.getId(),
                                report.getReporterUuid(),
                                report.getMessage(),
                                report.isResolved(),
                                report.getCreatedAt(),
                                report.getAttributes().getPlayerUuid()
                        );
                        addItem(item);
                    }
                }
        );
        apiManager.makeRequest(request);
    }
}
