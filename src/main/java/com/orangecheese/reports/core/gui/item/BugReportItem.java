package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.BugReportAttributes;
import com.orangecheese.reports.core.http.response.ReportData;

import java.util.ArrayList;

public class BugReportItem extends ReportItem<BugReportAttributes> {
    public BugReportItem(Window context, ReportData<BugReportAttributes> reportData) {
        super(context, reportData);
        setAdditionalArgument("Steps to reproduce", reportData.getAttributes().getStepsToReproduce());
    }

    @Override
    public ArrayList<WindowItem> buildAdditionalOptions(Window context) {
        return new ArrayList<>();
    }
}