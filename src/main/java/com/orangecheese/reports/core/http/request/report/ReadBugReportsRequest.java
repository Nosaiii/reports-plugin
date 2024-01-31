package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.BugReportAttributes;
import com.orangecheese.reports.core.http.response.ReportResponse;
import com.orangecheese.reports.core.http.response.ReportsResponse;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.utility.EmptyRecord;

import java.util.*;
import java.util.function.Consumer;

public class ReadBugReportsRequest extends HTTPRequestWithResponse<ReportsResponse<BugReportAttributes>, EmptyRecord> implements IHTTPBody {
    private final int page;

    private final boolean showResolved;

    private final boolean showUnresolved;

    private final ContainerCache containerCache;

    public ReadBugReportsRequest(int page, boolean showResolved, boolean showUnresolved, Consumer<ReportsResponse<BugReportAttributes>> onSuccess) {
        super("report/bug-reports", HTTPMethod.GET, onSuccess, response -> {});
        this.page = page;
        this.showResolved = showResolved;
        this.showUnresolved = showUnresolved;
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ReportsResponse<BugReportAttributes> parseResponse(JsonObject json) {
        JsonObject reportsRootObject = json.get("reports").getAsJsonObject();

        JsonArray reportsJsonArray = reportsRootObject.get("data").getAsJsonArray();
        Iterator<JsonElement> reportsIterator = reportsJsonArray.iterator();

        List<ReportResponse<BugReportAttributes>> reports = new ArrayList<>();

        while(reportsIterator.hasNext()) {
            JsonElement reportElement = reportsIterator.next();
            JsonObject reportObject = reportElement.getAsJsonObject();

            JsonObject bugReportObject = reportObject.get("bug_report").getAsJsonObject();
            int bugReportId = bugReportObject.get("id").getAsInt();
            String stepsToReproduce = bugReportObject.get("steps_to_reproduce").getAsString();
            BugReportAttributes bugReportAttributes = new BugReportAttributes(bugReportId, stepsToReproduce);

            ReportResponse<BugReportAttributes> report = ReportResponse.fromJson(reportObject, bugReportAttributes);
            reports.add(report);
        }

        ReportResponse<BugReportAttributes>[] reportsArray = reports.toArray(new ReportResponse[0]);
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