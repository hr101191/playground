package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class JobDetailsId implements Serializable {

    @Serial
    private static final long serialVersionUID = 4893958141885265677L;

    private String schedName;
    private String jobName;
    private String jobGroup;

    public JobDetailsId() {
    }

    public JobDetailsId(String schedName, String jobName, String jobGroup) {
        this.schedName = schedName;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobDetailsId that = (JobDetailsId) o;

        if (!schedName.equals(that.schedName)) return false;
        if (!jobName.equals(that.jobName)) return false;
        return jobGroup.equals(that.jobGroup);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + jobName.hashCode();
        result = 31 * result + jobGroup.hashCode();
        return result;
    }
}
