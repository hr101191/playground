package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_PAUSED_TRIGGER_GRPS")
@IdClass(PausedTriggerGrpsId.class)
public class PausedTriggerGrps implements Serializable {

    @Serial
    private static final long serialVersionUID = -6995800902932438463L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    public PausedTriggerGrps() {
    }

    public PausedTriggerGrps(String schedName, String triggerGroup) {
        this.schedName = schedName;
        this.triggerGroup = triggerGroup;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
}
