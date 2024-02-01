package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.response.ReportssData;
import com.orangecheese.reports.core.http.response.SuggestionReportAttributes;

import java.util.function.Consumer;

public class ReadSuggestionReportsRequest extends ReadReportsRequest<SuggestionReportAttributes> {
    public ReadSuggestionReportsRequest(int page, boolean showResolved, boolean showUnresolved, Consumer<ReportssData<SuggestionReportAttributes>> onSuccess) {
        super("suggestion-reports", "suggestion_report", page, showResolved, showUnresolved, onSuccess);
    }

    @Override
    public SuggestionReportAttributes parseAttributes(JsonObject json) {
        int id = json.get("id").getAsInt();
        return new SuggestionReportAttributes(id);
    }
}