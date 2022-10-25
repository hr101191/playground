package com.hurui.controller.api;

import com.hurui.model.TriggerRequest;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/triggers")
public class TriggersRestController {

    private static final Logger logger = LoggerFactory.getLogger(TriggersRestController.class);

    private final SchedulerFactoryBean schedulerFactoryBean;

    public TriggersRestController(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @GetMapping
    public ResponseEntity<? extends List<? extends Trigger>> getAll() {
        try {
            List<? extends Trigger> triggers = new ArrayList<>();
            for(JobKey jobKey: this.schedulerFactoryBean.getScheduler().getJobKeys(GroupMatcher.anyJobGroup())) {
                this.schedulerFactoryBean.getScheduler().getTriggersOfJob(jobKey);
            }
            return ResponseEntity.ok(triggers);
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping
    public void addTrigger(@RequestBody TriggerRequest triggerRequest) {
        this.schedulerFactoryBean.setTriggers(TriggerBuilder.newTrigger().withIdentity(triggerRequest.getTriggerKey()).build());
    }

    @PutMapping("/{calendarName}")
    public void addTriggerByCalendarName(@PathVariable String calendarName, @RequestBody TriggerRequest triggerRequest) {
        this.schedulerFactoryBean.setTriggers(TriggerBuilder.newTrigger().withIdentity(triggerRequest.getTriggerKey()).modifiedByCalendar(calendarName).build());
    }

    @DeleteMapping("/unschedule")
    public ResponseEntity<?> unscheduleJobByTriggerKey(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "group", required = false, defaultValue = "DEFAULT") String group
    ) {
        try {
            Optional<String> nameOptional = Optional.ofNullable(name);
            if(nameOptional.isPresent()) {
                if(this.schedulerFactoryBean.getScheduler().checkExists(TriggerKey.triggerKey(nameOptional.get(), group))) {
                    this.schedulerFactoryBean.getScheduler().unscheduleJob(TriggerKey.triggerKey(nameOptional.get(), group));
                    logger.info("Unscheduled job using TriggerKey. Trigger Key: {}", TriggerKey.triggerKey(nameOptional.get(), group));
                    return ResponseEntity.noContent().build();
                }
                logger.warn("Trigger not found. Trigger Key: {}", TriggerKey.triggerKey(nameOptional.get(), group));
                return ResponseEntity.notFound().build();
            } else {
                this.schedulerFactoryBean.getScheduler().unscheduleJobs(new ArrayList<>(this.schedulerFactoryBean.getScheduler().getTriggerKeys(GroupMatcher.anyTriggerGroup())));
                return ResponseEntity.noContent().build();
            }
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
            if(this.schedulerFactoryBean.getScheduler().checkExists(TriggerKey.triggerKey(name, group))) {
                this.schedulerFactoryBean.getScheduler().pauseTrigger(TriggerKey.triggerKey(name, group));
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
            if(this.schedulerFactoryBean.getScheduler().checkExists(TriggerKey.triggerKey(name, group))) {
                this.schedulerFactoryBean.getScheduler().resumeTrigger(TriggerKey.triggerKey(name, group));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
