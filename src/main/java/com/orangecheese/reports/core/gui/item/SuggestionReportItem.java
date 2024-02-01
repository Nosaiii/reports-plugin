package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.core.http.response.SuggestionReportAttributes;

public class SuggestionReportItem extends ReportItem<SuggestionReportAttributes> {
    public SuggestionReportItem(Window context, ReportData<SuggestionReportAttributes> reportData) {
        super(context, reportData);
    }
}