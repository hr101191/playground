package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class SchedulerStateId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2195600625461263025L;

    private String schedName;
    private String instanceName;

    public SchedulerStateId() {
    }

    public SchedulerStateId(String schedName, String instanceName) {
        this.schedName = schedName;
        this.instanceName = instanceName;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchedulerStateId that = (SchedulerStateId) o;

        if (!schedName.equals(that.schedName)) return false;
        return instanceName.equals(that.instanceName);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + instanceName.hashCode();
        return result;
    }
}
