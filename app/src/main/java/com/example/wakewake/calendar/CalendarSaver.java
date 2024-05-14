package com.example.wakewake.calendar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.wakewake.calendar.event.VAlarm;
import com.example.wakewake.data.Alarm;
import com.example.wakewake.data.CalendarSingleton;

import java.util.List;

public class CalendarSaver {

    public static void saveCalendar(Context context, Calendar calendar) {
        SharedPreferences preferences = context.getSharedPreferences("calendar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("calendar", calendar.toString());
        editor.putString("link", CalendarSingleton.getInstance().getCalendarLink());
        editor.apply();
    }

    public static Boolean loadCalendar(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("calendar", Context.MODE_PRIVATE);
        if (!preferences.contains("calendar")) {
            return false;
        }
        CalendarSingleton.getInstance().setCalendar(CalendarBuilder.build(preferences), null);
        return true;
    }

    public static void saveAlarms(Context context, List<Alarm> alarms) {
        SharedPreferences preferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String alarmsString = "";
        for (Alarm alarm : alarms) {
            alarmsString += alarm.toString();
        }
        editor.putString("alarms", alarmsString);
        editor.apply();
    }

    public static void loadAlarms(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        if (!preferences.contains("alarms")) {
            return;
        }
        CalendarSingleton.getInstance().setAlarms(CalendarBuilder.buildAlarms(preferences));
    }
}
