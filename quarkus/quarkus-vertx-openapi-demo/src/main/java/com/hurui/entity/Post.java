package com.hurui.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RevisionType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Audited(withModifiedFlag = true)
@Table(name = "post")
@DataObject
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

    @Column(name = "display_name", length = 300, nullable = false)
    private String displayName;

    @Column(name = "likes", nullable = false)
    private Long likes;

    @Column(name = "dislikes", nullable = false)
    private Long dislikes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @NotAudited
    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotAudited
    @UpdateTimestamp
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @Transient
    private RevisionType revisionType;

    @Transient
    private Set<String> modifiedFields;

    public Post() {
    }

    public Post(Long id, String title, String text, String displayName, Long likes, Long dislikes, List<Comment> comments, ZonedDateTime createdDate, ZonedDateTime lastModifiedDate, RevisionType revisionType, Set<String> modifiedFields) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.displayName = displayName;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.revisionType = revisionType;
        this.modifiedFields = modifiedFields;
    }

    public Post(JsonObject jsonObject) {
        Post post = jsonObject.mapTo(Post.class);
        this.setId(post.getId());
        this.setTitle(post.getTitle());
        this.setText(post.getText());
        this.setDisplayName(post.getDisplayName());
        this.setLikes(post.getLikes());
        this.setDislikes(post.getDislikes());
        this.setDislikes(post.getDislikes());
        this.setComments(post.getComments());
        this.setCreatedDate(post.getCreatedDate());
        this.setLastModifiedDate(post.getLastModifiedDate());
        this.setRevisionType(post.getRevisionType());
        this.setModifiedFields(post.getModifiedFields());
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    public Set<String> getModifiedFields() {
        return modifiedFields;
    }

    public void setModifiedFields(Set<String> modifiedFields) {
        this.modifiedFields = modifiedFields;
    }
}
