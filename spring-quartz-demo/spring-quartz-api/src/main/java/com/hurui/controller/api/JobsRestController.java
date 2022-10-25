package com.hurui.controller.api;

import com.hurui.model.JobRequest;
import com.hurui.model.JobResponse;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobsRestController {

    private static final Logger logger = LoggerFactory.getLogger(JobsRestController.class);

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final List<? extends QuartzJobBean> qualifiedJobs;


    public JobsRestController(SchedulerFactoryBean schedulerFactoryBean, List<? extends QuartzJobBean> qualifiedJobs) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.qualifiedJobs = qualifiedJobs;
    }

    @GetMapping("/qualified-job-class-names")
    public ResponseEntity<List<String>> getAllQualifiedJobClassNames() {
        try {
            List<JobDetail> jobDetailList = new ArrayList<>();
            for(String jobGroupName: this.schedulerFactoryBean.getScheduler().getJobGroupNames()) {
                for (JobKey jobKey : this.schedulerFactoryBean.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName))) {
                    jobDetailList.add(this.schedulerFactoryBean.getScheduler().getJobDetail(jobKey));
                }
            }
            Set<String> qualifiedJobNames = jobDetailList.stream()
                    .map(jobDetail -> jobDetail.getJobClass().getName())
                    .collect(Collectors.toSet());
            this.qualifiedJobs.forEach(job -> {
                qualifiedJobNames.add(job.getClass().getName());
            });
            return ResponseEntity.ok(new ArrayList<>(qualifiedJobNames));
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @GetMapping
    public ResponseEntity<List<JobDetail>> getAllPersistedJobs() {
        try {
            List<JobDetail> jobDetailList = new ArrayList<>();
            for(String jobGroupName: this.schedulerFactoryBean.getScheduler().getJobGroupNames()) {
                for (JobKey jobKey : this.schedulerFactoryBean.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName))) {
                    jobDetailList.add(this.schedulerFactoryBean.getScheduler().getJobDetail(jobKey));
                }
            }
            return ResponseEntity.ok(jobDetailList);
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<JobResponse> getJobByJobKey(
            @PathVariable(name = "name") String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group
    ) {
        try {
            if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(name, group))) {
                JobDetail jobDetail = this.schedulerFactoryBean.getScheduler().getJobDetail(JobKey.jobKey(name, group));
                List<? extends Trigger> triggers = this.schedulerFactoryBean.getScheduler()
                        .getTriggersOfJob(JobKey.jobKey(name, group));
//                        .stream()
//                        .map(Trigger.class::cast)
//                        .toList();
                JobResponse jobResponse = new JobResponse(jobDetail, triggers);
                return ResponseEntity.ok(jobResponse);
            }
            return ResponseEntity.notFound().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addJob(
            @RequestParam(name = "replace", required = false, defaultValue = "false") boolean replace,
            @RequestParam(name = "storeNonDurableWhileAwaitingScheduling", required = false, defaultValue = "true") boolean storeNonDurableWhileAwaitingScheduling,
            @RequestBody JobRequest jobRequest
    ) {
        try {
            this.schedulerFactoryBean.getScheduler().addJob(jobRequest.buildJobDetail(), replace, storeNonDurableWhileAwaitingScheduling);
        } catch (SchedulerException | ClassNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping
    public ResponseEntity<?> modifyJob(
            @RequestParam(name = "replace", required = false, defaultValue = "false") boolean replace,
            @RequestBody JobRequest jobRequest
    ) {
        try {
            JobDetail jobDetail = jobRequest.buildJobDetail();
            if(this.schedulerFactoryBean.getScheduler().checkExists(jobDetail.getKey())) {
                this.schedulerFactoryBean.getScheduler().addJob(jobDetail, replace);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SchedulerException | ClassNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteJob(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group
    ) {
        try {
            Optional<String> nameOptional = Optional.ofNullable(name);
            if(nameOptional.isPresent()) {
                if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(nameOptional.get(), group))) {
                    this.schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(nameOptional.get(), group));
                    return ResponseEntity.noContent().build();
                }
                logger.warn("Job not found using JobKey. Job Key: {}", JobKey.jobKey(nameOptional.get(), group));
                return ResponseEntity.notFound().build();
            }
            this.schedulerFactoryBean.getScheduler().deleteJobs(new ArrayList<>(this.schedulerFactoryBean.getScheduler().getJobKeys(GroupMatcher.anyJobGroup())));
            return ResponseEntity.noContent().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/unschedule")
    public ResponseEntity<?> unscheduleJobByJobKey(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group
    ) {
        try {
            Optional<String> nameOptional = Optional.ofNullable(name);
            if(nameOptional.isPresent()) {
                if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(nameOptional.get(), group))) {
                    for(Trigger trigger : this.schedulerFactoryBean.getScheduler().getTriggersOfJob(JobKey.jobKey(nameOptional.get(), group))) {
                        this.schedulerFactoryBean.getScheduler().unscheduleJob(trigger.getKey());
                        logger.info("Unscheduled job using TriggerKey. Trigger Key: {}", trigger.getKey());
                    }
                    return ResponseEntity.noContent().build();
                }
                logger.warn("Trigger not found using JobKey. Job Key: {}", JobKey.jobKey(name, group));
                return ResponseEntity.notFound().build();
            }
            this.schedulerFactoryBean.getScheduler().unscheduleJobs(new ArrayList<>(this.schedulerFactoryBean.getScheduler().getTriggerKeys(GroupMatcher.anyTriggerGroup())));
            return ResponseEntity.noContent().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping("/pause")
    public ResponseEntity<?> pause(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group
    ) {
        try {
            if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(name, group))) {
                this.schedulerFactoryBean.getScheduler().pauseJob(JobKey.jobKey(name, group));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping("/resume")
    public ResponseEntity<?> resume(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group
    ) {
        try {
            if(this.schedulerFactoryBean.getScheduler().checkExists(JobKey.jobKey(name, group))) {
                this.schedulerFactoryBean.getScheduler().resumeJob(JobKey.jobKey(name, group));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

}
