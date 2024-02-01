package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.core.http.response.SuggestionReportAttributes;

import java.util.ArrayList;

public class SuggestionReportItem extends ReportItem<SuggestionReportAttributes> {
    public SuggestionReportItem(Window context, ReportData<SuggestionReportAttributes> reportData) {
        super(context, reportData);
    }

    @Override
    public ArrayList<WindowItem> buildAdditionalOptions(Window context) {
        return new ArrayList<>();
    }
}