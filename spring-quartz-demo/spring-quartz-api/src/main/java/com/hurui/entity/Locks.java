package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_LOCKS")
@IdClass(LocksId.class)
public class Locks implements Serializable {

    @Serial
    private static final long serialVersionUID = -4935185156828181277L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "LOCK_NAME", nullable = false)
    private String lockName;

    public Locks() {
    }

    public Locks(String schedName, String lockName) {
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

}
