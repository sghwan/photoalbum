package com.squarecross.photoalbum.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "photo", schema = "photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "photo_id")})
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "file_name", unique = false, nullable = true)
    private String fileName;

    @Column(name = "original_url", unique = false, nullable = true)
    private String originalUrl;

    @Column(name = "thumb_url", unique = false, nullable = true)
    private String thumbUrl;

    @Column(name = "file_size", unique = false, nullable = true)
    private int fileSize;

    @CreationTimestamp
    @Column(name = "uploaded_at", unique = false, nullable = true)
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
