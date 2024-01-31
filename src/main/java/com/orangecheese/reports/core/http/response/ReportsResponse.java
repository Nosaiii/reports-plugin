package com.orangecheese.reports.core.http.response;

public class ReportsResponse<T> {
    private final ReportResponse<T>[] reports;

    public ReportsResponse(ReportResponse<T>[] reports) {
        this.reports = reports;
    }

    public ReportResponse<T>[] getReports() {
        return reports;
    }
}