package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_JOB_DETAILS")
@IdClass(JobDetailsId.class)
public class JobDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = -8960622755869058896L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "JOB_NAME", nullable = false)
    private String jobName;

    @Id
    @Column(name = "JOB_GROUP", nullable = false)
    private String jobGroup;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "JOB_CLASS_NAME", nullable = false)
    private String jobClassName;

    @Column(name = "IS_DURABLE", columnDefinition = "VARCHAR(1)", nullable = false)
    private boolean isDurable;

    @Column(name = "IS_NONCONCURRENT", columnDefinition = "VARCHAR(1)", nullable = false)
    private boolean isNonconcurrent;

    @Column(name = "IS_UPDATE_DATA", columnDefinition = "VARCHAR(1)", nullable = false)
    private boolean isUpdateData;

    @Column(name = "REQUESTS_RECOVERY", columnDefinition = "VARCHAR(1)", nullable = false)
    private boolean requestsRecovery;

    @Lob
    @Column(name = "JOB_DATA", columnDefinition = "BLOB")
    private Byte[] jobData;

    public JobDetails() {
    }

    public JobDetails(String schedName, String jobName, String jobGroup, String description, String jobClassName, boolean isDurable, boolean isNonconcurrent, boolean isUpdateData, boolean requestsRecovery, Byte[] jobData) {
        this.schedName = schedName;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.jobClassName = jobClassName;
        this.isDurable = isDurable;
        this.isNonconcurrent = isNonconcurrent;
        this.isUpdateData = isUpdateData;
        this.requestsRecovery = requestsRecovery;
        this.jobData = jobData;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
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

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public boolean getIsDurable() {
        return isDurable;
    }

    public void setDurable(boolean isDurable) {
        this.isDurable = isDurable;
    }

    public boolean getIsNonconcurrent() {
        return isNonconcurrent;
    }

    public void setIsNonconcurrent(boolean isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    public boolean getIsUpdateData() {
        return isUpdateData;
    }

    public void setIsUpdateData(boolean isUpdateData) {
        this.isUpdateData = isUpdateData;
    }

    public boolean getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(boolean requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public Byte[] getJobData() {
        return jobData;
    }

    public void setJobData(Byte[] jobData) {
        this.jobData = jobData;
    }
}
