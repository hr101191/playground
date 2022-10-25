package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_TRIGGERS")
@IdClass(TriggersId.class)
public class Triggers implements Serializable {

    @Serial
    private static final long serialVersionUID = -8595935549957705878L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false)
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    @Column(name = "JOB_NAME", nullable = false)
    private String jobName;

    @Column(name = "JOB_GROUP", nullable = false)
    private String jobGroup;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NEXT_FIRE_TIME")
    private Long nextFireTime;

    @Column(name = "PREV_FIRE_TIME")
    private Long prevFireTime;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "TRIGGER_STATE", nullable = false)
    private String triggerState;

    @Column(name = "TRIGGER_Type", nullable = false)
    private String triggerType;

    @Column(name = "START_TIME", nullable = false)
    private long startTime;

    @Column(name = "END_TIME")
    private Long endTime;

    @Column(name = "CALENDAR_NAME")
    private String calendarName;

    @Column(name = "MISFIRE_INSTR")
    private Short misfireInstr;

    @Lob
    @Column(name = "JOB_DATA", columnDefinition = "BLOB")
    private Byte[] jobData;

    public Triggers() {
    }

    public Triggers(String schedName, String triggerName, String triggerGroup, String jobName, String jobGroup, String description, Long nextFireTime, Long prevFireTime, Integer priority, String triggerState, String triggerType, long startTime, Long endTime, String calendarName, Short misfireInstr, Byte[] jobData) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.nextFireTime = nextFireTime;
        this.prevFireTime = prevFireTime;
        this.priority = priority;
        this.triggerState = triggerState;
        this.triggerType = triggerType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarName = calendarName;
        this.misfireInstr = misfireInstr;
        this.jobData = jobData;
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Long getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(Long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Short getMisfireInstr() {
        return misfireInstr;
    }

    public void setMisfireInstr(Short misfireInstr) {
        this.misfireInstr = misfireInstr;
    }

    public Byte[] getJobData() {
        return jobData;
    }

    public void setJobData(Byte[] jobData) {
        this.jobData = jobData;
    }
}
