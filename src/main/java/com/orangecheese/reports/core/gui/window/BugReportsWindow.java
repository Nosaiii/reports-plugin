package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.BugReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.ReportsWindow;
import com.orangecheese.reports.core.http.request.report.ReadBugReportsRequest;
import com.orangecheese.reports.core.http.response.BugReportAttributes;
import com.orangecheese.reports.core.http.response.ReportData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BugReportsWindow extends ReportsWindow<BugReportAttributes, ReadBugReportsRequest> {
    public BugReportsWindow(Player player, WindowHistory history) {
        super(player, "Bug reports", history);
    }

    @Override
    public ReadBugReportsRequest prepareRequest(int page, boolean showResolved, boolean showUnresolved) {
        return new ReadBugReportsRequest(
                page,
                showResolved,
                showUnresolved,
                response -> {
                    for(ReportData<BugReportAttributes> report : response.getReports()) {
                        BugReportItem item = new BugReportItem(this, report);
                        addItem(item);
                    }
                });
    }
}
