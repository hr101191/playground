package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class TriggersId implements Serializable {

    @Serial
    private static final long serialVersionUID = -3507629874985082503L;

    private String schedName;
    private String triggerName;
    private String triggerGroup;

    public TriggersId() {
    }

    public TriggersId(String schedName, String triggerName, String triggerGroup) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
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

        TriggersId that = (TriggersId) o;

        if (!schedName.equals(that.schedName)) return false;
        if (!triggerName.equals(that.triggerName)) return false;
        return triggerGroup.equals(that.triggerGroup);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + triggerName.hashCode();
        result = 31 * result + triggerGroup.hashCode();
        return result;
    }
}
