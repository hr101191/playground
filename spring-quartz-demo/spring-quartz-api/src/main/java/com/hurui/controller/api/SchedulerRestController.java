package com.hurui.controller.api;

import com.hurui.model.JobRequest;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerRestController {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public SchedulerRestController(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @GetMapping
    public ResponseEntity<SchedulerMetaData> getMetaData() {
        try {
            return ResponseEntity.ok(this.schedulerFactoryBean.getScheduler().getMetaData());
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PostMapping
    public ResponseEntity<?> triggerJob(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group,
            @RequestBody(required = false) Map<String, Object> jobMap
    ) {
        try {
            if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(name, group))) {
                JobDataMap jobDataMap = new JobDataMap();
                Optional.ofNullable(jobMap).ifPresent(jobDataMap::putAll);
                if(jobDataMap.isEmpty()) {
                    this.schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(name, group));
                } else {
                    this.schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(name, group), jobDataMap);
                }
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PostMapping("/noop/{jobName}")
    public ResponseEntity<?> scheduleJobUsingNoopTrigger(
            @PathVariable(required = false) String jobName,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String jobGroup,
            @RequestBody(required = false)JobRequest jobRequest
    ) {
        try {
            Optional<String> jobNameOptional = Optional.ofNullable(jobName);
            if(jobNameOptional.isPresent()) {
                if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(jobNameOptional.get(), jobGroup))) {
                    JobDetail jobDetail = this.schedulerFactoryBean.getScheduler().getJobDetail(JobKey.jobKey(jobNameOptional.get(), jobGroup));
                    this.schedulerFactoryBean.getScheduler().scheduleJob(
                            TriggerBuilder.newTrigger()
                                    .withIdentity("MT_" + UUID.randomUUID())
                                    .forJob(jobDetail)
                                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionNextWithRemainingCount())
                                    .startNow()
                                    .build()
                    );
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.notFound().build();
            }
            this.schedulerFactoryBean.getScheduler().scheduleJob(
                    TriggerBuilder.newTrigger()
                            .withIdentity("MT_" + UUID.randomUUID())
                            .forJob(jobRequest.buildJobDetail())
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionNextWithRemainingCount())
                            .startNow()
                            .build()
            );
            return ResponseEntity.noContent().build();
        } catch (SchedulerException | ClassNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping("/pause-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pauseAllJobs() {
        try {
            this.schedulerFactoryBean.getScheduler().pauseAll();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping("/resume-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resumeAllJobs() {
        try {
            this.schedulerFactoryBean.getScheduler().resumeAll();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
