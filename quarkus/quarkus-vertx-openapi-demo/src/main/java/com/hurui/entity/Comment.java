package com.hurui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Audited(withModifiedFlag = true)
@Table(name = "comment")
@DataObject
public class Comment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 7522151012033553944L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_post_id"))
    private Post post;

    @Column(name = "text", length = 20000, nullable = false)
    private String text;

    @Column(name = "display_name", length = 300, nullable = false)
    private String displayName;

    @NotAudited
    @Column(name = "likes", nullable = false)
    private Long likes;

    @NotAudited
    @Column(name = "dislikes", nullable = false)
    private Long dislikes;

    @NotAudited
    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotAudited
    @UpdateTimestamp
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    public Comment() {
    }

    public Comment(Long id, String text, String displayName, Long likes, Long dislikes, ZonedDateTime createdDate, ZonedDateTime lastModifiedDate) {
        this.id = id;
        this.text = text;
        this.displayName = displayName;
        this.likes = likes;
        this.dislikes = dislikes;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Comment(JsonObject jsonObject) {
        Comment comment = jsonObject.mapTo(Comment.class);
        this.setId(comment.getId());
        this.setText(comment.getText());
        this.setDisplayName(comment.getDisplayName());
        this.setLikes(comment.getLikes());
        this.setDislikes(comment.getDislikes());
        this.setDislikes(comment.getDislikes());
        this.setCreatedDate(comment.getCreatedDate());
        this.setLastModifiedDate(comment.getLastModifiedDate());
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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
