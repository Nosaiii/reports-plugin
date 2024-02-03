package com.orangecheese.reports.utility;

import com.orangecheese.reports.ReportsPlugin;

import java.time.*;
import java.util.logging.Level;

public final class DateUtility {
    public static LocalDateTime convertFromGMT(LocalDateTime date, String timezone) {
        ZoneId sourceZone = ZoneId.of("GMT");

        ZoneId targetZone;
        try {
            targetZone = ZoneId.of(timezone);
        } catch(DateTimeException e) {
            ReportsPlugin.getInstance().getLogger().log(Level.WARNING, "Invalid timezone of '" + timezone + "' given.");
            return date;
        }

        ZonedDateTime sourceZonedDate = ZonedDateTime.of(date, sourceZone);
        ZonedDateTime targetZonedDate = sourceZonedDate.withZoneSameInstant(targetZone);

        return targetZonedDate.toLocalDateTime();
    }
}