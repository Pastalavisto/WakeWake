package com.example.wakewake.data;

import android.util.Log;

import com.example.wakewake.calendar.Calendar;
import com.example.wakewake.calendar.Event;
import com.example.wakewake.calendar.Utils;
import com.example.wakewake.calendar.event.VAlarm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class CalendarSingleton {
    private static CalendarSingleton instance = null;
    private String calendarLink;
    private Calendar calendar;
    private static final Comparator<Event> componentComparator = Comparator.comparing(o -> o.getProperty("DTSTART").get());

    private CalendarSingleton() {
        this.calendar = null;
    }

    public static CalendarSingleton getInstance() {
        if (CalendarSingleton.instance == null) {
            synchronized (CalendarSingleton.class) {
                if (CalendarSingleton.instance == null) {
                    CalendarSingleton.instance = new CalendarSingleton();
                }
            }
        }
        return CalendarSingleton.instance;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar, String calendarLink) {
        this.calendar = calendar;
        this.calendarLink = calendarLink;
        Log.i("CalendarSingleton", "Calendar set");
    }

    public ArrayList<CalendarDay> getDays() {
        ArrayList<CalendarDay> days = new ArrayList<>();
        if (getCalendar() == null) {
            Log.i("CalendarSingleton", "getDays: calendar is null");
            return days;
        }
        List<Event> events = getCalendar().getEvents();
        events = events.stream().sorted(componentComparator).collect(Collectors.toList());
        for (Event event : events) {
            if (event.getDtEnd().isBefore(LocalDateTime.now(ZoneId.of("UTC"))))
                continue;
            boolean found = false;
            for (CalendarDay day : days) {
                String date = Utils.formatDateCalendar(day.getDateTime());
                if (event.getProperty("DTSTART").isPresent()) {
                    LocalDateTime eventDate = event.getDtStart();
                    String eventDateString = Utils.formatDateCalendar(eventDate);
                    if (date.equals(eventDateString)) {
                        day.getEvents().add(event);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                ArrayList<Event> newEvents = new ArrayList<>();
                newEvents.add(event);
                days.add(new CalendarDay((LocalDateTime) event.getDtStart(), newEvents));
            }
        }
        return days;
    }
    public void createAutomaticAlarmBeforeFistEvent(int hour, int minute, String summary) {
        if (getCalendar() == null) {
            Log.i("CalendarSingleton", "createAutomaticAlarmBeforeFistEvent: calendar is null");
            return;
        }
        ArrayList<CalendarDay> days = getDays();
        if (days.isEmpty()) {
            Log.i("CalendarSingleton", "createAutomaticAlarmBeforeFistEvent: no events");
            return;
        }
        for (CalendarDay day : days) {
            if (!day.getEvents().isEmpty()) {
                LocalDateTime startDate = day.getDateTime();
                addAlarm(summary, startDate.minusHours(hour).minusMinutes(minute), startDate.minusHours(hour).minusMinutes(minute), null, null);
            }
        }
    }

    public void addAlarm(String summary, LocalDateTime startDate, LocalDateTime endDate, String location, String description) {
        Event alarm = new VAlarm();
        alarm.setSummary(summary);
        alarm.setDtStart(startDate);
        alarm.setDtEnd(endDate);
        if (location != null) {
            alarm.setLocation(location);
        }
        if (description != null) {
            alarm.setDescription(description);
        }
        getCalendar().addEvent(alarm);
    }

}
