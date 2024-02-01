package com.orangecheese.reports.core.http.response;

public class ReportssData<T> {
    private final ReportData<T>[] reports;

    public ReportssData(ReportData<T>[] reports) {
        this.reports = reports;
    }

    public ReportData<T>[] getReports() {
        return reports;
    }
}