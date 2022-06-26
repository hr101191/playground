package com.hurui.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.codegen.annotations.DataObject;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Audited(withModifiedFlag = true)
@Table(name = "comment")
@DataObject(generateConverter = true)
public class Comment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 7522151012033553944L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "likes", nullable = false)
    private Long likes;

    @Column(name = "dislikes", nullable = false)
    private Long dislikes;

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

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public void setDislikes(Long dislikes) {
        this.dislikes = dislikes;
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
