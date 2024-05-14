package com.example.wakewake;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.wakewake.calendar.CalendarSaver;
import com.example.wakewake.calendar.event.VAlarm;
import com.example.wakewake.data.Alarm;
import com.example.wakewake.data.CalendarSingleton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
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
        if (CalendarSingleton.getInstance().getCalendar() == null) {
            Toast.makeText(getApplication().getApplicationContext(), "Aucun calendrier présent", Toast.LENGTH_SHORT).show();
            return;
        }
        Alarm alarm = CalendarSingleton.getInstance().createAutomaticAlarm(hour, minute);
        if (alarm != null) {
            Toast.makeText(getApplication().getApplicationContext(), "Réveil crée", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication().getApplicationContext(), "Réveil déjà présent", Toast.LENGTH_SHORT).show();
        }
        AlarmScheduler.scheduleNextAlarm(getApplication().getApplicationContext());
    }

    public List<Alarm> getAlarms() {
        return CalendarSingleton.getInstance().getAlarmsSorted();
    }

    public void deleteAlarm(Alarm alarm) {
        CalendarSingleton.getInstance().deleteAlarm(alarm);
        AlarmScheduler.scheduleNextAlarm(getApplication().getApplicationContext());
    }
}