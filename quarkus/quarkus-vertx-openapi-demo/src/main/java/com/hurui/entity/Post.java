package com.hurui.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited(withModifiedFlag = true)
@Table(name = "post")
@DataObject(generateConverter = true)
public class Post extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = -4785888052205745953L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "text", nullable = false, length = 20000)
    private String text;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "comments")
    private List<Comment> comments = new ArrayList<>();

    @NotAudited
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotAudited
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    public Post() {
    }

    public Post(Long id, String title, String text, List<Comment> comments, ZonedDateTime createdDate, ZonedDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.comments = comments;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Post(JsonObject jsonObject) {
        PostConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        PostConverter.toJson(this, jsonObject);
        return jsonObject;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
