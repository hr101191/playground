package com.hurui.model;

import org.quartz.Calendar;

public class CalendarRequest {

    private String name;
    private Calendar calendar;
    private boolean replace;
    private boolean updateTriggers;

    public CalendarRequest() {
    }

    public CalendarRequest(String name, Calendar calendar, boolean replace, boolean updateTriggers) {
        this.name = name;
        this.calendar = calendar;
        this.replace = replace;
        this.updateTriggers = updateTriggers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean getReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public boolean getUpdateTriggers() {
        return updateTriggers;
    }

    public void setUpdateTriggers(boolean updateTriggers) {
        this.updateTriggers = updateTriggers;
    }
}
