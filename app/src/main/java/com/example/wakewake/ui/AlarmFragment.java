package com.example.wakewake.ui;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.wakewake.AlarmViewModel;
import com.example.wakewake.R;
import com.example.wakewake.calendar.Utils;
import com.example.wakewake.calendar.event.VAlarm;
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
        List<VAlarm> alarms = viewModel.getAlarms();
        LinearLayout alarmsLayout = view.findViewById(R.id.alarmList);
        alarmsLayout.removeAllViews();
        for (VAlarm alarm : alarms) {
            addAlarm(alarmsLayout, alarm);
        }
    }

    private void addAlarm(LinearLayout alarmsLayout, VAlarm alarm) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View alarmView = inflater.inflate(R.layout.alarm_item, alarmsLayout, false);
        TextView alarmTime = alarmView.findViewById(R.id.alarmTime);
        alarmTime.setText(Utils.formatTime(alarm.getDtStart()));
        TextView alarmDate = alarmView.findViewById(R.id.alarmDate);
        alarmDate.setText(alarm.getSummary());
        TextView alarmCountDown = alarmView.findViewById(R.id.alarmCountdown);
        alarmCountDown.setText(Utils.formatCountDown(alarm.getDtStart()));
        alarmsLayout.addView(alarmView);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                viewModel.createAlarm(selectedHour, selectedMinute);
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), onTimeSetListener, viewModel.getCurrentHour(), viewModel.getCurrentMinute(), true);

        timePickerDialog.setTitle("Heure du réveil");
        timePickerDialog.show();
    }
}