package com.example.wakewake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.wakewake.calendar.AlarmSoundManager;
import com.example.wakewake.calendar.CalendarSaver;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CalendarSaver.loadAlarms(context);
        CalendarSaver.loadCalendar(context);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());

        AlarmSoundManager.playAlarm(context);
        AlarmScheduler.scheduleNextAlarm(context);

    }

}

