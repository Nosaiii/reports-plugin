package com.orangecheese.reports.core.http.response;

public class BugReportAttributes {
    private final int id;

    private final String stepsToReproduce;

    public BugReportAttributes(int id, String stepsToReproduce) {
        this.id = id;
        this.stepsToReproduce = stepsToReproduce;
    }

    public int getId() {
        return id;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }
}