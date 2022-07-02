package com.hurui.envers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "revision_info")
@RevisionEntity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RevisionInfo implements Serializable {

    private static final long serialVersionUID = -8297802449357109766L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "id", nullable = false)
    private Long id;

    @RevisionTimestamp
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevisionInfo that = (RevisionInfo) o;
        return Objects.equals(id, that.id) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp);
    }

    @Override
    public String toString() {
        return "RevisionInfo{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }
}
