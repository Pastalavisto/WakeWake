package com.example.wakewake;

import androidx.lifecycle.ViewModel;

import com.example.wakewake.data.CalendarSingleton;

public class SettingsViewModel extends ViewModel {

    public String getCalendarUrl() {
        return CalendarSingleton.getInstance().getCalendarLink() == null ? "Aucun calendrier sélectionné" : CalendarSingleton.getInstance().getCalendarLink().length() > 50 ? CalendarSingleton.getInstance().getCalendarLink().substring(0, 50) + "..." : CalendarSingleton.getInstance().getCalendarLink();
    }
}