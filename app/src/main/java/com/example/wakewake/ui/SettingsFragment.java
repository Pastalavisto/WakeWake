package com.example.wakewake.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wakewake.CalendarViewModel;
import com.example.wakewake.R;
import com.example.wakewake.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private SettingsViewModel viewModel;

    private Button btnCalendar;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCalendar=view.findViewById(R.id.changeCalendarButton);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarChoiceFragment dialogFragment=new CalendarChoiceFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(),"CalendarChoiceFragment");
            }
        });
    }

}