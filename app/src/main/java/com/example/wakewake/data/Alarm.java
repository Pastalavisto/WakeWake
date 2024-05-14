package com.example.wakewake.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wakewake.calendar.event.VAlarm;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Alarm {

    private long duration;
    private boolean isOn;

    public Alarm(long duration, boolean isOn) {
        this.duration = duration;
        this.isOn = isOn;
    }

    public Alarm() {
        this(0, false);
    }

    public VAlarm getVAlarm(LocalDateTime dateTime) {
        VAlarm vAlarm = new VAlarm();
        LocalDateTime alarmTime = dateTime.minusMinutes(duration);
        vAlarm.setDtStart(alarmTime);
        vAlarm.setDtEnd(alarmTime);
        vAlarm.setSummary("Réveil automatique");
        return vAlarm;
    }

    public VAlarm getVAlarm(){
        if (getNextDay() == null) {
            Log.i("Alarm", "No next day");
            return null;
        }
        Log.i("Alarm", "Next day: " + getNextDay().getDateTime());
        return getVAlarm(getNextDay().getDateTime());
    }

    public CalendarDay getNextDay() {
        List<CalendarDay> days = CalendarSingleton.getInstance().getDays();
        for (CalendarDay day : days) {
            if (day.getAlarms().contains(this)) {
                return day;
            }
        }
        return null;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void toggle() {
        isOn = !isOn;
    }

    public long getDurationNextAlarm() {
        CalendarDay nextDay = getNextDay();
        if (nextDay == null) {
            return Long.MAX_VALUE;
        }
        VAlarm vAlarm = getVAlarm(nextDay.getDateTime());
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), vAlarm.getDtStart());
    }

    public String getDurationString() {
        String hours = String.valueOf(duration / 60);
        String minutes = String.valueOf(duration % 60);
        return (hours.length() == 1 ? "0" + hours : hours) + ":" + (minutes.length() == 1 ? "0" + minutes : minutes);
    }

    @NonNull
    @Override
    public String toString() {
        return "BEGIN:ALARM\nDURATION:" + duration + "\nON:" + isOn + "\nEND:ALARM\n";
    }
}
