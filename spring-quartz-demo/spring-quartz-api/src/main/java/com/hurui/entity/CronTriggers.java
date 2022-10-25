package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_CRON_TRIGGERS")
@IdClass(TriggersId.class)
public class CronTriggers implements Serializable {

    @Serial
    private static final long serialVersionUID = -4380920238389892294L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false)
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    @Column(name = "CRON_EXPRESSION", nullable = false)
    private String cronExpression;

    @Column(name = "TIME_ZONE_ID")
    private String timeZoneId;

    public CronTriggers() {
    }

    public CronTriggers(String schedName, String triggerName, String triggerGroup, String cronExpression, String timeZoneId) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.cronExpression = cronExpression;
        this.timeZoneId = timeZoneId;
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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
}
