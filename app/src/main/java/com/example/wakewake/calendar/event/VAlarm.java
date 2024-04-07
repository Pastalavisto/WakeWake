package com.example.wakewake.calendar.event;

import com.example.wakewake.calendar.Event;
import com.example.wakewake.calendar.Utils;

public class VAlarm extends Event {
    public String getName() {
        return "VALARM";
    }

    @Override
    public String toString() {
        return "BEGIN:" + getName() + "\n" +
                "SUMMARY:" + getSummary() + "\n" +
                "LOCATION:" + getLocation() + "\n" +
                "DESCRIPTION:" + getDescription() + "\n" +
                "DTSTART:" + Utils.formatDateToIcal(getDtStart()) + "\n" +
                "DTEND:" + Utils.formatDateToIcal(getDtEnd()) + "\n" +
                "END:" + getName();
    }
}
