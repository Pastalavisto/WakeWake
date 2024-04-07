package com.example.wakewake.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    private static final String patternIcal = "yyyyMMdd'T'HHmmss'Z'";

    public static String formatDateCalendar(LocalDateTime date) {
        String dateString = date.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.getDefault()));
        String [] parts = dateString.split(" ");
        return parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1,3) + " " + parts[1] + " " + parts[2].substring(0, 1).toUpperCase() + parts[2].substring(1,3);
    }

    public static String formatTime(LocalDateTime date) {
        ZonedDateTime zonedDateTime = date.atZone(ZoneId.of("UTC"));
        ZonedDateTime localDateTime = zonedDateTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());
        String dateString = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()));
        Log.i("Utils", "formatTime: " + dateString);
        return dateString;
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternIcal);
        LocalDateTime utcDateTime = LocalDateTime.parse(date, formatter);
        Log.i("Utils", "parseDate: " + utcDateTime);
        return utcDateTime;
    }

    public static String formatDateToIcal(LocalDateTime date) {
        LocalDateTime utcDateTime = date.atOffset(ZoneOffset.UTC).toLocalDateTime();
        String dateString = utcDateTime.format(DateTimeFormatter.ofPattern(patternIcal));
        Log.i("Utils", "formatDateToIcal: " + dateString);
        return dateString;
    }
}
