package com.hurui.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DummyJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(DummyJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//        JobDataMap mergedJobDataMap = context.getJobDetail().getJobDataMap();
//        mergedJobDataMap.putAll(context.getTrigger().getJobDataMap().getWrappedMap());
        logger.info("Job executed: {} | {}", context.getJobDetail().getJobDataMap().size(), context.getTrigger().getJobDataMap().size());
    }
}