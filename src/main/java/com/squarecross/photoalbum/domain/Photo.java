package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "photo", schema = "photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "photo_id")})
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "thumb_url")
    private String thumbUrl;

    @Column(name = "file_size")
    private int fileSize;

    @CreationTimestamp
    @Column(name = "uploaded_at")
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    //==생성 메서드==//
    public static Photo createPhoto(Long albumId, Album album, String fileName, int size) {
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setFileSize(size);
        photo.setOriginalUrl("\\photos\\original\\" + albumId + "\\" + fileName);
        photo.setThumbUrl("\\photos\\thumb\\" + albumId + "\\" + fileName);
        photo.setAlbum(album);
        return photo;
    }

    //==연관관계 편의 메서드==//
    public void setAlbum(Album album) {
        this.album = album;
        album.getPhotos().add(this);
    }

    public void updateAlbum(Album album, String movedOriginalFilePath, String movedThumbFilePath) {
        this.setAlbum(album);
        this.setOriginalUrl("\\photos\\original\\" + movedOriginalFilePath);
        this.setThumbUrl("\\photos\\thumb\\" + movedThumbFilePath);
    }
}
