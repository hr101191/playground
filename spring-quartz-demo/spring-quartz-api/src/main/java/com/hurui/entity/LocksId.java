package com.hurui.entity;

import java.io.Serial;
import java.io.Serializable;

public class LocksId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7519261496504480172L;

    private String schedName;
    private String lockName;

    public LocksId() {
    }

    public LocksId(String schedName, String lockName) {
        this.schedName = schedName;
        this.lockName = lockName;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocksId locksId = (LocksId) o;

        if (!schedName.equals(locksId.schedName)) return false;
        return lockName.equals(locksId.lockName);
    }

    @Override
    public int hashCode() {
        int result = schedName.hashCode();
        result = 31 * result + lockName.hashCode();
        return result;
    }
}
