package com.orangecheese.reports.core.http.request.report;

import com.google.gson.JsonObject;
import com.orangecheese.reports.core.http.response.*;

import java.util.UUID;
import java.util.function.Consumer;

public class ReadPlayerReportsRequest extends ReadReportsRequest<PlayerReportAttributes> {
    public ReadPlayerReportsRequest(int page, boolean showResolved, boolean showUnresolved, Consumer<ReportssData<PlayerReportAttributes>> onSuccess) {
        super("player-reports", "player_report", page, showResolved, showUnresolved, onSuccess);
    }

    @Override
    public PlayerReportAttributes parseAttributes(JsonObject json) {
        int id = json.get("id").getAsInt();
        UUID playerUuid = UUID.fromString(json.get("player_uuid").getAsString());
        return new PlayerReportAttributes(id, playerUuid);
    }
}