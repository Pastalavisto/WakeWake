package com.example.wakewake.data;



import android.util.Log;

import com.example.wakewake.calendar.Event;
import com.example.wakewake.calendar.event.VAlarm;
import com.example.wakewake.calendar.event.VEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalendarDay {
    private LocalDateTime dateTime;
    private ArrayList<Event> events;
    private ArrayList<Alarm> alarms;
    private int idOfFirstVEvent;

    public CalendarDay(LocalDateTime dateTime, ArrayList<Event> events) {
        this.dateTime = dateTime;
        this.events = events;
        this.alarms = new ArrayList<>();
        idOfFirstVEvent = 0;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void addVAlarm(Alarm alarm) {
        VAlarm vAlarm = alarm.getVAlarm(dateTime);

        for (int i = idOfFirstVEvent; i >= 0; i--) {
            if (events.get(i) instanceof VAlarm) {
                if (events.get(i).getDtStart().isBefore(vAlarm.getDtStart())) {
                    events.add(i + 1, vAlarm);
                    addAlarm(alarm);
                    idOfFirstVEvent += 1;
                    return;
                }
            }
        }
        events.add(0, vAlarm);
        addAlarm(alarm);
        idOfFirstVEvent += 1;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }
}
