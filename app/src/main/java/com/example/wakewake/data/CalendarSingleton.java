package com.example.wakewake.data;

import android.util.Log;
import android.widget.Toast;

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
    private List<Alarm> alarms = new ArrayList<>();
    private static final Comparator<Event> componentComparator = Comparator.comparing(o -> o.getProperty("DTSTART").get());
    private static final Comparator<Alarm> alarmComparator = Comparator.comparing(Alarm::getDurationNextAlarm);

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
        for (Alarm alarm : getInstance().getAlarms()) {
            for (CalendarDay day : days) {
                if (alarm.isOn() && LocalDateTime.now(ZoneId.of("UTC")).isBefore(day.getDateTime().minusMinutes(alarm.getDuration()))) {
                    day.addVAlarm(alarm);
                }
            }
        }
        return days;
    }
    public Alarm createAutomaticAlarm(int hour, int minute) {
        for (Alarm alarm : alarms) {
            if (alarm.getDuration() == hour * 60L + minute) {
                alarm.setOn(true);
                return null;
            }
        }
        Alarm alarm = new Alarm(hour * 60L + minute, true);
        getInstance().getAlarms().add(alarm);
        return alarm;
    }

    public List<Alarm> getAlarmsSorted() {
        return this.alarms.stream().sorted(alarmComparator).collect(Collectors.toList());
    }

    public List<Alarm> getAlarms() {
        return this.alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    public String getCalendarLink() {
        return calendarLink;
    }

    public Alarm getNextAlarm() {
        return this.alarms.stream().filter(Alarm::isOn).min(alarmComparator).orElse(null);
    }

    public void deleteAlarm(Alarm alarm) {
        this.alarms.remove(alarm);
    }
}
