package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_SIMPLE_TRIGGERS")
@IdClass(TriggersId.class)
public class SimpleTriggers implements Serializable {

    @Serial
    private static final long serialVersionUID = -5245590498474631541L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false)
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    @Column(name = "REPEAT_COUNT", nullable = false)
    private long repeatCount;

    @Column(name = "REPEAT_INTERVAL", nullable = false)
    private long repeatInterval;

    @Column(name = "TIMES_TRIGGERED", nullable = false)
    private long timesTriggered;

    public SimpleTriggers() {
    }

    public SimpleTriggers(String schedName, String triggerName, String triggerGroup, long repeatCount, long repeatInterval, long timesTriggered) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
        this.timesTriggered = timesTriggered;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public long getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(long repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public long getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(long timesTriggered) {
        this.timesTriggered = timesTriggered;
    }
}
