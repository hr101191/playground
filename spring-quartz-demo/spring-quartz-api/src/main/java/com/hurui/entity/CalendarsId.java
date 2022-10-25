package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class CalendarsId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2082288164458755358L;

    private String schedName;
    private String calendarName;

    public CalendarsId() {
    }

    public CalendarsId(String schedName, String calendarName) {
        this.schedName = schedName;
        this.calendarName = calendarName;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarsId that = (CalendarsId) o;

        if (!schedName.equals(that.schedName)) return false;
        return calendarName.equals(that.calendarName);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + calendarName.hashCode();
        return result;
    }
}
