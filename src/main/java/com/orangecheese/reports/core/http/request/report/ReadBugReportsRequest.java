package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.response.BugReportAttributes;
import com.orangecheese.reports.core.http.response.ReportssData;

import java.util.function.Consumer;

public class ReadBugReportsRequest extends ReadReportsRequest<BugReportAttributes> {
    public ReadBugReportsRequest(int page, boolean showResolved, boolean showUnresolved, Consumer<ReportssData<BugReportAttributes>> onSuccess) {
        super("bug-reports", "bug_report", page, showResolved, showUnresolved, onSuccess);
    }

    @Override
    public BugReportAttributes parseAttributes(JsonObject json) {
        int bugReportId = json.get("id").getAsInt();
        String stepsToReproduce = json.get("steps_to_reproduce").getAsString();
        return new BugReportAttributes(bugReportId, stepsToReproduce);
    }
}