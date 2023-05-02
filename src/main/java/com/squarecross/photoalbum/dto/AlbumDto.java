package com.squarecross.photoalbum.dto;

import com.squarecross.photoalbum.domain.Album;

import java.util.Date;

public class AlbumDto {

    private Long albumId;
    private String albumName;
    private Date createdAt;
    private int count;

    public AlbumDto(Album album) {
        albumId = album.getId();
        albumName = album.getName();
        createdAt = album.getCreatedAt();
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
