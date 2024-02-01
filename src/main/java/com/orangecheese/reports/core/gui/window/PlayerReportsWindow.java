package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.PlayerReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.ReportsWindow;
import com.orangecheese.reports.core.http.request.report.ReadPlayerReportsRequest;
import com.orangecheese.reports.core.http.response.PlayerReportAttributes;
import com.orangecheese.reports.core.http.response.ReportData;
import org.bukkit.entity.Player;

public class PlayerReportsWindow extends ReportsWindow<PlayerReportAttributes, ReadPlayerReportsRequest> {
    public PlayerReportsWindow(Player player, WindowHistory history) {
        super(player, "Player reports", history);
    }

    @Override
    public ReadPlayerReportsRequest prepareRequest(int page, boolean showResolved, boolean showUnresolved) {
        return new ReadPlayerReportsRequest(
                page,
                showResolved,
                showUnresolved,
                response -> {
                    for (ReportData<PlayerReportAttributes> report : response.getReports()) {
                        PlayerReportItem item = new PlayerReportItem(this, report);
                        addItem(item);
                    }
                }
        );
    }
}
