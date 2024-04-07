package com.example.wakewake.calendar;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public abstract class Event {
    private String Summary;
    private String Description;
    private LocalDateTime DtStart;
    private LocalDateTime DtEnd;
    private String Location;
    public abstract String getName();

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDateTime getDtStart() {
        return DtStart;
    }

    public void setDtStart(LocalDateTime dtStart) {
        DtStart = dtStart;
    }

    public LocalDateTime getDtEnd() {
        return DtEnd;
    }

    public void setDtEnd(LocalDateTime dtEnd) {
        DtEnd = dtEnd;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Optional<String> getProperty(String property) {
        switch (property) {
            case "SUMMARY":
                return Optional.ofNullable(Summary);
            case "DESCRIPTION":
                return Optional.ofNullable(Description);
            case "DTSTART":
                return Optional.ofNullable(DtStart.toString());
            case "DTEND":
                return Optional.ofNullable(DtEnd.toString());
            case "LOCATION":
                return Optional.ofNullable(Location);
            default:
                return Optional.empty();
        }
    }
}
