package com.squarecross.photoalbum.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "album", schema = "photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "album_id")})
public class Album {

    protected Album() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "album_name", unique = false, nullable = false)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", unique = false, nullable = true)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
