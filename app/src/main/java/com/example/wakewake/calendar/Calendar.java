package com.example.wakewake.calendar;

import android.util.Log;

import com.example.wakewake.calendar.event.VAlarm;
import com.example.wakewake.calendar.event.VEvent;

import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private List<Event> events;

    public Calendar(List<Event> events) {
        this.events = events;
    }

    public Calendar() {
        this(new ArrayList<>());
    }

    public List<VEvent> getVEvents() {
        List<VEvent> result = new ArrayList<>();
        for (Event event : events) {
            if (event instanceof VEvent) {
                result.add((VEvent) event);
            }
        }
        return result;
    }

    public List<VAlarm> getVAlarm() {
        List<VAlarm> result = new ArrayList<>();
        for (Event event : events) {
            if (event instanceof VAlarm) {
                result.add((VAlarm) event);
            }
        }
        return result;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    @Override
    public String toString() {
        String res = "";

        for (Event event : getEvents()) {
            res += event + "\n";
        }
        Log.d("Calendar", res);
        return res;
    }
}
