package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class FiredTriggersId implements Serializable {

    @Serial
    private static final long serialVersionUID = 3291859935642260096L;

    private String schedName;
    private String entryId;

    public FiredTriggersId() {
    }

    public FiredTriggersId(String schedName, String entryId) {
        this.schedName = schedName;
        this.entryId = entryId;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FiredTriggersId that = (FiredTriggersId) o;

        if (!schedName.equals(that.schedName)) return false;
        return entryId.equals(that.entryId);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + entryId.hashCode();
        return result;
    }
}
