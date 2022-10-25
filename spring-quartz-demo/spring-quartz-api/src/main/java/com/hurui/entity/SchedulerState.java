package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_SCHEDULER_STATE")
@IdClass(SchedulerStateId.class)
public class SchedulerState implements Serializable {

    @Serial
    private static final long serialVersionUID = 5958238300050178642L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "INSTANCE_NAME", nullable = false)
    private String instanceName;

    @Column(name = "LAST_CHECKIN_TIME", nullable = false)
    private long lastCheckinTime;

    @Column(name = "CHECKIN_INTERVAL", nullable = false)
    private long checkinInterval;

    public SchedulerState() {
    }

    public SchedulerState(String schedName, String instanceName, long lastCheckinTime, long checkinInterval) {
        this.schedName = schedName;
        this.instanceName = instanceName;
        this.lastCheckinTime = lastCheckinTime;
        this.checkinInterval = checkinInterval;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public long getLastCheckinTime() {
        return lastCheckinTime;
    }

    public void setLastCheckinTime(long lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public long getCheckinInterval() {
        return checkinInterval;
    }

    public void setCheckinInterval(long checkinInterval) {
        this.checkinInterval = checkinInterval;
    }
}
