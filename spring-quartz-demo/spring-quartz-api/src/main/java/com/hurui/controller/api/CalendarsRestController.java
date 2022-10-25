package com.hurui.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hurui.job.DummyJob;
import com.hurui.model.CalendarRequest;
import org.quartz.*;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.calendar.DailyCalendar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendars")
public class CalendarsRestController {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public CalendarsRestController(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllCalendarNames() {
        try {
            return ResponseEntity.ok(this.schedulerFactoryBean.getScheduler().getCalendarNames());
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @GetMapping
    public ResponseEntity<List<Calendar>> getAll() {
        try {
            try {
                Calendar c = new CronCalendar("* * 0-7,18-23 ? * *");
                Calendar d = new DailyCalendar(1, 1, 1, 1, 2, 1, 1, 1);
                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println(objectMapper.writeValueAsString(d));
                JobDetail job = JobBuilder.newJob(DummyJob.class).storeDurably().build();
                JobDetail job1 = JobBuilder.newJob(DummyJob.class).storeDurably().build();
                this.schedulerFactoryBean.getScheduler().scheduleJob(job, TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString()).forJob(job).withSchedule(SimpleScheduleBuilder.repeatHourlyForever().withMisfireHandlingInstructionNextWithRemainingCount()).startNow().build());
                this.schedulerFactoryBean.getScheduler().scheduleJob(
                        job1,
                        TriggerBuilder.newTrigger()
                                .withIdentity(UUID.randomUUID().toString())
                                .forJob(job1)
                                .withSchedule(CronScheduleBuilder.cronSchedule("59 59 23 31 12 ? 2099").withMisfireHandlingInstructionDoNothing()).startNow()
                                .build()
                );
            } catch (ParseException | JsonProcessingException | SchedulerException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            List<Calendar> calendars = new ArrayList<>();
            for(String calendarName: this.schedulerFactoryBean.getScheduler().getCalendarNames()) {
                calendars.add(this.schedulerFactoryBean.getScheduler().getCalendar(calendarName));
            }
            return ResponseEntity.ok(calendars);
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @GetMapping("/{calendarName}")
    public ResponseEntity<Calendar> getByCalendarName(@PathVariable String calendarName) {
        try {
            Optional<Calendar> calendarOptional = Optional.ofNullable(this.schedulerFactoryBean.getScheduler().getCalendar(calendarName));
            if(calendarOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(calendarOptional.get());
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void add(@RequestBody CalendarRequest calendarRequest) {
        try {
            this.schedulerFactoryBean.getScheduler().addCalendar(calendarRequest.getName(), calendarRequest.getCalendar(), calendarRequest.getReplace(), calendarRequest.getUpdateTriggers());
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        try {
            for(String calendarName: this.schedulerFactoryBean.getScheduler().getCalendarNames()) {
                this.schedulerFactoryBean.getScheduler().deleteCalendar(calendarName);
            }
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{calendarName}")
    public ResponseEntity<?> deleteByCalendarName(@PathVariable String calendarName) {
        try {
            Optional<Calendar> calendarOptional = Optional.ofNullable(this.schedulerFactoryBean.getScheduler().getCalendar(calendarName));
            if(calendarOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            this.schedulerFactoryBean.getScheduler().deleteCalendar(calendarName);
            return ResponseEntity.noContent().build();
        } catch (SchedulerException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
