package com.orangecheese.reports.core.http.response;

public class ReportsMetaResponse {
    private final int bugReports;

    private final int playerReports;

    private final int suggestionReports;

    public ReportsMetaResponse(int bugReports, int playerReports, int suggestionReports) {
        this.bugReports = bugReports;
        this.playerReports = playerReports;
        this.suggestionReports = suggestionReports;
    }

    public int getBugReports() {
        return bugReports;
    }

    public int getPlayerReports() {
        return playerReports;
    }

    public int getSuggestionReports() {
        return suggestionReports;
    }
}