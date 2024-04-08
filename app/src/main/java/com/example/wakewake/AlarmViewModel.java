package com.example.wakewake;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.wakewake.calendar.CalendarSaver;
import com.example.wakewake.data.CalendarSingleton;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmViewModel extends AndroidViewModel {

    public AlarmViewModel(@NonNull Application application) {
        super(application);
    }

    public int getCurrentHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String[] time = sdf.format(System.currentTimeMillis()).split(":");
        return Integer.parseInt(time[0]);
    }

    public int getCurrentMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String[] time = sdf.format(System.currentTimeMillis()).split(":");
        return Integer.parseInt(time[1]);
    }

    public void createAlarm(int hour, int minute) {
        CalendarSingleton.getInstance().createAutomaticAlarmBeforeFistEvent(hour, minute, "Réveil automatique");
        CalendarSaver.saveCalendar(getApplication(), CalendarSingleton.getInstance().getCalendar());
    }
}