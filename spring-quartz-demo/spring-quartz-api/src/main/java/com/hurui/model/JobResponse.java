package com.hurui.model;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.List;

public class JobResponse {
    private JobDetail jobDetail;
    private List<? extends Trigger> triggers = new ArrayList<>();

    public JobResponse() {
    }

    public JobResponse(JobDetail jobDetail, List<? extends Trigger> triggers) {
        this.jobDetail = jobDetail;
        this.triggers = triggers;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public List<? extends Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<? extends Trigger> triggers) {
        this.triggers = triggers;
    }
}
