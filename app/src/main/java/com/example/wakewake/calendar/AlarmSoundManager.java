package com.example.wakewake.calendar;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmSoundManager {
    private static final MediaPlayer mp = new MediaPlayer();

    public static void playAlarm(Context context) {
        Uri url = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);

        try {
            mp.reset();
            mp.setDataSource(context, url);
            mp.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
            mp.prepare();
            mp.start();
            mp.setLooping(true);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mp.stop();
                    mp.release();
                }
            }, 25000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopAlarm() {
        if (mp.isPlaying()){
            mp.stop();
            mp.release();
        }
    }
}
