package com.hurui.model;

import org.quartz.*;

import java.util.Map;

public class JobRequest {

    private JobKey jobKey;
    private String jobClass;
    private String description;
    private Map<String, Object> jobData;
    private boolean storeDurably;
    private boolean requestRecovery;

    public JobRequest() {
    }

    public JobRequest(JobKey jobKey, String jobClass, String description, Map<String, Object> jobData, boolean storeDurably, boolean requestRecovery) {
        this.jobKey = jobKey;
        this.jobClass = jobClass;
        this.description = description;
        this.jobData = jobData;
        this.storeDurably = storeDurably;
        this.requestRecovery = requestRecovery;
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    public boolean isStoreDurably() {
        return storeDurably;
    }

    public void setStoreDurably(boolean storeDurably) {
        this.storeDurably = storeDurably;
    }

    public boolean isRequestRecovery() {
        return requestRecovery;
    }

    public void setRequestRecovery(boolean requestRecovery) {
        this.requestRecovery = requestRecovery;
    }

    @SuppressWarnings("unchecked")
    public JobDetail buildJobDetail() throws ClassNotFoundException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(this.getJobData());
        return JobBuilder.newJob()
                .withIdentity(this.jobKey)
                .withDescription(this.description)
                .ofType((Class<? extends Job>) Class.forName(this.jobClass))
                .setJobData(jobDataMap)
                .storeDurably(this.storeDurably)
                .requestRecovery(this.requestRecovery)
                .build();
    }
}
