package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class PausedTriggerGrpsId implements Serializable {

    @Serial
    private static final long serialVersionUID = 6503456043300173382L;

    private String schedName;
    private String triggerGroup;

    public PausedTriggerGrpsId() {
    }

    public PausedTriggerGrpsId(String schedName, String triggerGroup) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PausedTriggerGrpsId that = (PausedTriggerGrpsId) o;

        if (!schedName.equals(that.schedName)) return false;
        return triggerGroup.equals(that.triggerGroup);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + triggerGroup.hashCode();
        return result;
    }
}
