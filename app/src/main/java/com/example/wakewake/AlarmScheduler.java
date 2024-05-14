package com.example.wakewake;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.wakewake.data.Alarm;
import com.example.wakewake.data.CalendarSingleton;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AlarmScheduler {
    public static void scheduleAlarm(Context context, LocalDateTime localDateTime) {
        long alarmTimeMillis = localDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent,FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
            } else {
                context.startActivity(new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }
        Log.i("AlarmScheduler", "Alarm scheduled for " + localDateTime.atZone(ZoneId.of("UTC")).toString());
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    public static void scheduleNextAlarm(Context context) {
        cancelAlarm(context);
        Alarm nextAlarm = CalendarSingleton.getInstance().getNextAlarm();
        if (nextAlarm == null) {
            Log.i("AlarmScheduler", "No alarm to schedule");
            return;
        }
        scheduleAlarm(context, nextAlarm.getVAlarm().getDtStart());
    }
}
