package com.hurui.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Audited(withModifiedFlag = true)
@Table(name = "topic")
public class Topic extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = -4785888052205745953L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotAudited
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotAudited
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        this.setCreatedDate(ZonedDateTime.now());
        this.setLastModifiedDate(ZonedDateTime.now());
    }

    @PreUpdate
    private void preUpdate() {
        this.setLastModifiedDate(ZonedDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
