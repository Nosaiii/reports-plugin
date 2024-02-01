package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.ReportData;
import com.orangecheese.reports.core.http.response.ReportssData;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.utility.EmptyRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public abstract class ReadReportsRequest<T> extends HTTPRequestWithResponse<ReportssData<T>, EmptyRecord> implements IHTTPBody {
    private final String jsonAttributesKey;

    private final int page;

    private final boolean showResolved;

    private final boolean showUnresolved;

    private final ContainerCache containerCache;

    public ReadReportsRequest(String endpoint, String jsonAttributesKey, int page, boolean showResolved, boolean showUnresolved, Consumer<ReportssData<T>> onSuccess) {
        super("report/" + endpoint, HTTPMethod.GET, onSuccess, response -> {});
        this.jsonAttributesKey = jsonAttributesKey;
        this.page = page;
        this.showResolved = showResolved;
        this.showUnresolved = showUnresolved;
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    public abstract T parseAttributes(JsonObject json);

    @Override
    @SuppressWarnings("unchecked")
    public ReportssData<T> parseResponse(JsonObject json) {
        JsonObject reportsRootObject = json.get("reports").getAsJsonObject();

        JsonArray reportsJsonArray = reportsRootObject.get("data").getAsJsonArray();
        Iterator<JsonElement> reportsIterator = reportsJsonArray.iterator();

        List<ReportData<T>> reports = new ArrayList<>();

        while(reportsIterator.hasNext()) {
            JsonElement reportElement = reportsIterator.next();
            JsonObject reportObject = reportElement.getAsJsonObject();

            JsonObject attributesObject = reportObject.get(jsonAttributesKey).getAsJsonObject();
            T bugReportAttributes = parseAttributes(attributesObject);

            ReportData<T> report = ReportData.fromJson(reportObject, bugReportAttributes);
            reports.add(report);
        }

        ReportData<T>[] reportsArray = reports.toArray(new ReportData[0]);
        return new ReportssData<>(reportsArray);
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