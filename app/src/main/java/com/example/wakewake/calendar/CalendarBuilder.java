package com.example.wakewake.calendar;

import static com.example.wakewake.calendar.Utils.parseDate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import com.example.wakewake.calendar.event.VAlarm;
import com.example.wakewake.calendar.event.VEvent;
import com.example.wakewake.data.Alarm;
import com.example.wakewake.data.CalendarSingleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarBuilder {
    public static Calendar build(InputStream input) throws IOException {

        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("BEGIN:VEVENT")) {
                    events.add(buildVEvent(reader));
                }
            }
        }
        Log.i("CalendarBuilder", "Calendar built from scratch, Events: " + events.size());
        return new Calendar(events);
    }

    public static Calendar build(SharedPreferences mPrefs) {
        List<Event> events = new ArrayList<>();
        String calendarString = mPrefs.getString("calendar", "");
        if (calendarString.isEmpty()) {
            Log.i("CalendarBuilder", "No calendar in preferences");
        }
        Reader inputString = new StringReader(calendarString);
        BufferedReader reader = new BufferedReader(inputString);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("BEGIN:VEVENT")) {
                    events.add(buildVEvent(reader));
                }
            }
        } catch (IOException e) {
            Log.e("CalendarBuilder", "Error reading calendar from preferences", e);
        }
        Calendar calendar = new Calendar(events);
        Log.i("CalendarBuilder", "Calendar built from preferences");
        return calendar;
    }

    private static VEvent buildVEvent(BufferedReader reader) throws IOException {
        VEvent event = new VEvent();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("END:VEVENT")) {
                return event;
            }
            String[] parts = line.split(":");
            if (parts.length < 2) {
                continue;
            }
            String key = parts[0];
            String value = parts[1];
            if (value.equals("null")) {
                continue;
            }
            ZonedDateTime zonedDateTime;
            switch (key) {
                case "SUMMARY":
                    event.setSummary(value);
                    break;
                case "DTSTART":
                    event.setDtStart(parseDate(value));
                    break;
                case "DTEND":
                    event.setDtEnd(parseDate(value));
                    break;
                case "DESCRIPTION":
                    event.setDescription(value);
                    break;
                case "LOCATION":
                    event.setLocation(value);
                    break;
            }
        }
        return event;
    }
    public static List<Alarm> buildAlarms(SharedPreferences mPrefs) {
        List<Alarm> alarms = new ArrayList<>();
        String alarmsString = mPrefs.getString("alarms", "");
        if (alarmsString.isEmpty()) {
            Log.i("CalendarBuilder", "No alarms in preferences");
        }
        Reader inputString = new StringReader(alarmsString);
        BufferedReader reader = new BufferedReader(inputString);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("BEGIN:ALARM")) {
                    alarms.add(buildAlarm(reader));
                }
            }
        } catch (IOException e) {
            Log.e("CalendarBuilder", "Error reading alarms from preferences", e);
        }
        Log.i("CalendarBuilder", "Alarms built from preferences");
        return alarms;
    }

    private static Alarm buildAlarm(BufferedReader reader) throws IOException {
        Alarm alarm = new Alarm();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("END:ALARM")) {
                return alarm;
            }
            String[] parts = line.split(":");
            if (parts.length != 2) {
                continue;
            }
            String key = parts[0];
            String value = parts[1];
            if (value.equals("null")) {
                continue;
            }
            switch (key) {
                case "DURATION":
                    alarm.setDuration(Long.parseLong(value));
                    break;
                case "ON":
                    alarm.setOn(Boolean.parseBoolean(value));
                    break;
            }
        }

        return alarm;
    }
}
