package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.ReportResponse;
import com.orangecheese.reports.core.http.response.ReportsResponse;
import com.orangecheese.reports.core.http.response.SuggestionReportAttributes;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.utility.EmptyRecord;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ReadSuggestionReportsRequest extends HTTPRequestWithResponse<ReportsResponse<SuggestionReportAttributes>, EmptyRecord> implements IHTTPBody {
    private final int page;

    private final boolean showResolved;

    private final boolean showUnresolved;

    private final ContainerCache containerCache;

    public ReadSuggestionReportsRequest(int page, boolean showResolved, boolean showUnresolved, Consumer<ReportsResponse<SuggestionReportAttributes>> onSuccess) {
        super("report/suggestion-reports", HTTPMethod.GET, onSuccess, response -> {});
        this.page = page;
        this.showResolved = showResolved;
        this.showUnresolved = showUnresolved;
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ReportsResponse<SuggestionReportAttributes> parseResponse(JsonObject json) {
        JsonObject reportsRootObject = json.get("reports").getAsJsonObject();

        JsonArray reportsJsonArray = reportsRootObject.get("data").getAsJsonArray();
        Iterator<JsonElement> reportsIterator = reportsJsonArray.iterator();

        List<ReportResponse<SuggestionReportAttributes>> reports = new ArrayList<>();

        while(reportsIterator.hasNext()) {
            JsonElement reportElement = reportsIterator.next();
            JsonObject reportObject = reportElement.getAsJsonObject();

            JsonObject suggestionReportObject = reportObject.get("suggestion_report").getAsJsonObject();
            int suggestionReportId = suggestionReportObject.get("id").getAsInt();
            SuggestionReportAttributes suggestionReportAttributes = new SuggestionReportAttributes(suggestionReportId);

            ReportResponse<SuggestionReportAttributes> report = ReportResponse.fromJson(reportObject, suggestionReportAttributes);
            reports.add(report);
        }

        ReportResponse<SuggestionReportAttributes>[] reportsArray = reports.toArray(new ReportResponse[0]);
        return new ReportsResponse<>(reportsArray);
    }

    @Override
    public EmptyRecord parseFailureResponse(JsonObject json) {
        return new EmptyRecord();
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", containerCache.getAccessToken());
        json.addProperty("page", page);
        json.addProperty("showResolved", showResolved);
        json.addProperty("showUnresolved", showUnresolved);
        return json;
    }
}