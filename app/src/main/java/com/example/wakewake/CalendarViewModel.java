package com.example.wakewake;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.wakewake.calendar.Utils;
import com.example.wakewake.data.CalendarDay;
import com.example.wakewake.data.CalendarSingleton;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    public CalendarViewModel(@NonNull Application application) {
        super(application);
    }

    public List<String> getDaysString(){
        ArrayList<String> daysString = new ArrayList<>();
        ArrayList<CalendarDay> days = getDays();
        for (CalendarDay day : days) {
            daysString.add(Utils.formatDateCalendar(day.getDateTime()));
        }
        return daysString;
    }

    public ArrayList<CalendarDay> getDays () {
        return CalendarSingleton.getInstance().getDays();
    }
}
