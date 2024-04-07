package com.example.wakewake.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.wakewake.data.CalendarSingleton;

public class CalendarSaver {

    public static void saveCalendar(Context context, Calendar calendar) {
        SharedPreferences preferences = context.getSharedPreferences("calendar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("calendar", calendar.toString());
        editor.apply();
        Log.i("CalendarSaver", "Calendar saved");
        Log.d("CalendarSaver", calendar.toString());
    }

    public static Boolean loadCalendar(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("calendar", Context.MODE_PRIVATE);
        if (!preferences.contains("calendar")) {
            Log.d("CalendarSaver", "No calendar found");
            return false;
        }
        CalendarSingleton.getInstance().setCalendar(CalendarBuilder.build(preferences), null);
        return true;
    }
}
