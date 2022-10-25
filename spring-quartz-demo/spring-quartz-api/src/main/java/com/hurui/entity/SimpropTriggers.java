package com.hurui.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "QRTZ_SIMPROP_TRIGGERS")
@IdClass(TriggersId.class)
public class SimpropTriggers implements Serializable {

    @Serial
    private static final long serialVersionUID = 6098754973698509220L;

    @Id
    @Column(name = "SCHED_NAME", nullable = false)
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false)
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false)
    private String triggerGroup;

    @Column(name = "STR_PROP_1")
    private String strProp1;

    @Column(name = "STR_PROP_2")
    private String strProp2;

    @Column(name = "STR_PROP_3")
    private String strProp3;

    @Column(name = "INT_PROP_1")
    private Integer intProp1;

    @Column(name = "INT_PROP_2")
    private Integer intProp2;

    @Column(name = "LONG_PROP_1")
    private Long longProp1;

    @Column(name = "LONG_PROP_2")
    private Long longProp2;

    @Column(name = "DEC_PROP_1")
    private BigDecimal decProp1;

    @Column(name = "DEC_PROP_2")
    private BigDecimal decProp2;

    @Column(name = "BOOL_PROP_1")
    private Boolean boolProp1;

    @Column(name = "BOOL_PROP_2")
    private Boolean boolProp2;

    public SimpropTriggers() {
    }

    public SimpropTriggers(String schedName, String triggerName, String triggerGroup, String strProp1, String strProp2, String strProp3, Integer intProp1, Integer intProp2, Long longProp1, Long longProp2, BigDecimal decProp1, BigDecimal decProp2, Boolean boolProp1, Boolean boolProp2) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.strProp1 = strProp1;
        this.strProp2 = strProp2;
        this.strProp3 = strProp3;
        this.intProp1 = intProp1;
        this.intProp2 = intProp2;
        this.longProp1 = longProp1;
        this.longProp2 = longProp2;
        this.decProp1 = decProp1;
        this.decProp2 = decProp2;
        this.boolProp1 = boolProp1;
        this.boolProp2 = boolProp2;
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

    public String getStrProp1() {
        return strProp1;
    }

    public void setStrProp1(String strProp1) {
        this.strProp1 = strProp1;
    }

    public String getStrProp2() {
        return strProp2;
    }

    public void setStrProp2(String strProp2) {
        this.strProp2 = strProp2;
    }

    public String getStrProp3() {
        return strProp3;
    }

    public void setStrProp3(String strProp3) {
        this.strProp3 = strProp3;
    }

    public Integer getIntProp1() {
        return intProp1;
    }

    public void setIntProp1(Integer intProp1) {
        this.intProp1 = intProp1;
    }

    public Integer getIntProp2() {
        return intProp2;
    }

    public void setIntProp2(Integer intProp2) {
        this.intProp2 = intProp2;
    }

    public Long getLongProp1() {
        return longProp1;
    }

    public void setLongProp1(Long longProp1) {
        this.longProp1 = longProp1;
    }

    public Long getLongProp2() {
        return longProp2;
    }

    public void setLongProp2(Long longProp2) {
        this.longProp2 = longProp2;
    }

    public BigDecimal getDecProp1() {
        return decProp1;
    }

    public void setDecProp1(BigDecimal decProp1) {
        this.decProp1 = decProp1;
    }

    public BigDecimal getDecProp2() {
        return decProp2;
    }

    public void setDecProp2(BigDecimal decProp2) {
        this.decProp2 = decProp2;
    }

    public Boolean getBoolProp1() {
        return boolProp1;
    }

    public void setBoolProp1(Boolean boolProp1) {
        this.boolProp1 = boolProp1;
    }

    public Boolean getBoolProp2() {
        return boolProp2;
    }

    public void setBoolProp2(Boolean boolProp2) {
        this.boolProp2 = boolProp2;
    }
}
