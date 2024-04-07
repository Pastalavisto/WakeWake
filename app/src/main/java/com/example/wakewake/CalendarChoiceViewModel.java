package com.example.wakewake;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wakewake.calendar.Calendar;
import com.example.wakewake.calendar.CalendarBuilder;
import com.example.wakewake.calendar.CalendarSaver;
import com.example.wakewake.data.CalendarSingleton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CalendarChoiceViewModel extends AndroidViewModel {
    private String link;
    public MutableLiveData<Boolean> isDownloaded = new MutableLiveData<Boolean>(false);

    public CalendarChoiceViewModel(@NonNull Application application) {
        super(application);
    }

    public void setLink(String link) {
        this.link = link;
        downloadCalendar();
    }

    public String getLink() {
        return link;
    }

    public void downloadCalendar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // read iCal from url
                Calendar calendar;
                InputStream input = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(getLink()).openConnection();
                    connection.setRequestMethod("GET");
                    calendar = CalendarBuilder.build(connection.getInputStream());
                    isDownloaded.postValue(true);
                    CalendarSingleton.getInstance().setCalendar(calendar, getLink());
                    CalendarSaver.saveCalendar(getApplication(), calendar);
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