package com.example.wakewake.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wakewake.R;
import com.example.wakewake.calendar.CalendarSaver;
import com.example.wakewake.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    static ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (CalendarSaver.loadCalendar(this)) {
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
}
