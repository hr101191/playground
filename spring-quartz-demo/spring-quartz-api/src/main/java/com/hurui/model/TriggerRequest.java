package com.hurui.model;

import org.quartz.JobDetail;
import org.quartz.TriggerKey;

public class TriggerRequest {

    private TriggerKey triggerKey;
    private JobDetail jobDetail;

    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(TriggerKey triggerKey) {
        this.triggerKey = triggerKey;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }
}
