package com.example.wakewake.data;



import com.example.wakewake.calendar.Event;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarDay {
    private LocalDateTime dateTime;
    private ArrayList<Event> events;

    public CalendarDay(LocalDateTime dateTime, ArrayList<Event> events) {
        this.dateTime = dateTime;
        this.events = events;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
