package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "QRTZ_BLOB_TRIGGERS")
@IdClass(TriggersId.class)
public class BlobTriggers implements Serializable {

    @Serial
    private static final long serialVersionUID = -5353252033243337221L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false)
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    @Lob
    @Column(name = "BLOB_DATA", columnDefinition = "BLOB")
    private Byte[] blobData;

    public BlobTriggers() {
    }

    public BlobTriggers(String schedName, String triggerName, String triggerGroup, Byte[] blobData) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.blobData = blobData;
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

    public Byte[] getBlobData() {
        return blobData;
    }

    public void setBlobData(Byte[] blobData) {
        this.blobData = blobData;
    }
}
