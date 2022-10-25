package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_CALENDARS")
@IdClass(CalendarsId.class)
public class Calendars implements Serializable {

    @Serial
    private static final long serialVersionUID = 8511691345649871977L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "CALENDAR_NAME", nullable = false)
    private String calendarName;

    @Lob
    @Column(name = "CALENDAR", columnDefinition = "BLOB")
    private Byte[] calendar;

    public Calendars() {
    }

    public Calendars(String schedName, String calendarName, Byte[] calendar) {
        this.schedName = schedName;
        this.calendarName = calendarName;
        this.calendar = calendar;
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

    public Byte[] getCalendar() {
        return calendar;
    }

    public void setCalendar(Byte[] calendar) {
        this.calendar = calendar;
    }
}
