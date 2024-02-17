package com.orangecheese.reports.utility;

import com.orangecheese.reports.ReportsPlugin;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

public final class DateUtility {
    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

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

    public static LocalDateTime isoDateTimeToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, ISO_DATETIME_FORMATTER);
    }
}