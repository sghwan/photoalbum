package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;

public class AlbumMapper {

    public static AlbumDto convertToDto(Album album) {
        return new AlbumDto(album);
    }
}
