package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;

public class AlbumMapper {

    public static AlbumDto convertToDto(Album album) {
        return new AlbumDto(album);
    }

    public static Album convertToModel(AlbumDto albumDto) {
        Album album = new Album();
        album.setId(albumDto.getAlbumId());
        album.setName(albumDto.getAlbumName());
        album.setCreatedAt(album.getCreatedAt());
        return album;
    }
}
