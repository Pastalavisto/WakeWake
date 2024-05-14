package com.example.wakewake.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wakewake.AlarmScheduler;
import com.example.wakewake.R;
import com.example.wakewake.calendar.Calendar;
import com.example.wakewake.calendar.CalendarBuilder;
import com.example.wakewake.calendar.CalendarSaver;
import com.example.wakewake.data.CalendarSingleton;
import com.example.wakewake.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {

    static ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AlarmScheduler.scheduleNextAlarm(this);
        CalendarSaver.loadAlarms(this);
        if (CalendarSaver.loadCalendar(this)) {
            SharedPreferences preferences = getApplication().getSharedPreferences("calendar", Context.MODE_PRIVATE);
            if (preferences.contains("link")) {
                Log.i("MainActivity", "Link found in preferences");
                String link = preferences.getString("link", "");
                buildWithUrl(link);
            }
            replaceFragment(new CalendarFragment());
        } else {
            CalendarChoiceFragment dialogFragment=new CalendarChoiceFragment();
            dialogFragment.show(getSupportFragmentManager(),"CalendarChoiceFragment");
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_calendar) {
                replaceFragment(new CalendarFragment());
            } else if (item.getItemId() == R.id.navigation_alarm) {
                replaceFragment(new AlarmFragment());
            } else if (item.getItemId() == R.id.navigation_settings) {
                replaceFragment(new SettingsFragment());
            } else {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CalendarSaver.saveAlarms(getApplicationContext(), CalendarSingleton.getInstance().getAlarms());
        AlarmScheduler.scheduleNextAlarm(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CalendarSaver.saveAlarms(getApplicationContext(), CalendarSingleton.getInstance().getAlarms());
        AlarmScheduler.scheduleNextAlarm(this);
    }

    private void buildWithUrl(String link) {
        Log.i("MainActivity", "Building calendar with url");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // read iCal from url
                Calendar calendar;
                InputStream input = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
                    connection.setRequestMethod("GET");
                    calendar = CalendarBuilder.build(connection.getInputStream());
                    CalendarSingleton.getInstance().setCalendar(calendar, link);
                    CalendarSaver.saveCalendar(getApplicationContext(), calendar);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
