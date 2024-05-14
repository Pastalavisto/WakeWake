package com.example.wakewake.ui;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.wakewake.AlarmViewModel;
import com.example.wakewake.R;
import com.example.wakewake.calendar.Utils;
import com.example.wakewake.calendar.event.VAlarm;
import com.example.wakewake.data.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AlarmFragment extends Fragment {
    private AlarmViewModel viewModel;

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fabAlarmAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });
        List<Alarm> alarms = viewModel.getAlarms();
        LinearLayout alarmsLayout = view.findViewById(R.id.alarmList);
        alarmsLayout.removeAllViews();
        for (Alarm alarm : alarms) {
            addAlarm(alarmsLayout, alarm);
        }
    }

    private void addAlarm(LinearLayout alarmsLayout, Alarm alarm) {
        VAlarm valarm = alarm.getVAlarm();
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View alarmView = inflater.inflate(R.layout.alarm_item, alarmsLayout, false);
        TextView alarmTime = alarmView.findViewById(R.id.alarmTime);
        alarmTime.setText(alarm.getDurationString() + " avant le début des cours");
        TextView alarmDate = alarmView.findViewById(R.id.alarmDescription);
        if (valarm == null) {
            alarmDate.setText("Désactivé");
        } else {
            alarmDate.setText(valarm.getSummary() + " " + (alarm.isOn()?"dans "+Utils.formatCountDown(valarm.getDtStart()):"Désactivé"));
        }
        alarmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Supprimer l'alarme
                Log.i("AlarmFragment", "Alarm clicked");
                viewModel.deleteAlarm(alarm);
                MainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.navigation_alarm);
            }
        });

        SwitchCompat alarmSwitch = alarmView.findViewById(R.id.alarmSwitch);
        alarmSwitch.setChecked(alarm.isOn());

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                alarm.toggle();
                MainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.navigation_alarm);
            }
        });

        alarmsLayout.addView(alarmView);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                viewModel.createAlarm(selectedHour, selectedMinute);
                MainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.navigation_alarm);
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), onTimeSetListener, viewModel.getCurrentHour(), viewModel.getCurrentMinute(), true);

        timePickerDialog.setTitle("Heure du réveil");
        timePickerDialog.show();
    }
}