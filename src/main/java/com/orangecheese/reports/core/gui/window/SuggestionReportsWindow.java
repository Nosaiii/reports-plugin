package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.SuggestionReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.ReportsWindow;
import com.orangecheese.reports.core.http.request.report.ReadSuggestionReportsRequest;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.core.http.response.SuggestionReportAttributes;
import org.bukkit.entity.Player;

public class SuggestionReportsWindow extends ReportsWindow<SuggestionReportAttributes, ReadSuggestionReportsRequest> {
    public SuggestionReportsWindow(Player player, WindowHistory history) {
        super(player, "Suggestions", history);
    }

    @Override
    public ReadSuggestionReportsRequest prepareRequest(int page, boolean showResolved, boolean showUnresolved) {
        return new ReadSuggestionReportsRequest(
                page,
                showResolved,
                showUnresolved,
                response -> {
                    for(ReportData<SuggestionReportAttributes> report : response.getReports()) {
                        SuggestionReportItem item = new SuggestionReportItem(this, report);
                        addItem(item);
                    }
                }
        );
    }
}
