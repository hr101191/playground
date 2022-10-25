package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_FIRED_TRIGGERS")
@IdClass(FiredTriggersId.class)
public class FiredTriggers implements Serializable {

    @Serial
    private static final long serialVersionUID = -6569401984568618243L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "ENTRY_ID", nullable = false)
    private String entryId;

    @Column(name = "TRIGGER_NAME", nullable = false)
    private String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    @Column(name = "INSTANCE_NAME", nullable = false)
    private String instanceName;

    @Column(name = "FIRED_TIME", nullable = false)
    private long firedTime;

    @Column(name = "SCHED_TIME", nullable = false)
    private long schedTime;

    @Column(name = "PRIORITY", nullable = false)
    private int priority;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_GROUP")
    private String jobGroup;

    @Column(name = "IS_NONCONCURRENT", columnDefinition = "VARCHAR(1)")
    private Boolean isNonconcurrent;

    @Column(name = "REQUESTS_RECOVERY", columnDefinition = "VARCHAR(1)")
    private Boolean requestsRecovery;

    public FiredTriggers() {
    }

    public FiredTriggers(String schedName, String entryId, String triggerName, String triggerGroup, String instanceName, long firedTime, long schedTime, int priority, String state, String jobName, String jobGroup, Boolean isNonconcurrent, Boolean requestsRecovery) {
        this.schedName = schedName;
        this.entryId = entryId;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.instanceName = instanceName;
        this.firedTime = firedTime;
        this.schedTime = schedTime;
        this.priority = priority;
        this.state = state;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.isNonconcurrent = isNonconcurrent;
        this.requestsRecovery = requestsRecovery;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public long getFiredTime() {
        return firedTime;
    }

    public void setFiredTime(long firedTime) {
        this.firedTime = firedTime;
    }

    public long getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(long schedTime) {
        this.schedTime = schedTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Boolean getIsNonconcurrent() {
        return isNonconcurrent;
    }

    public void setIsNonconcurrent(Boolean isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    public Boolean getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(Boolean requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }
}
