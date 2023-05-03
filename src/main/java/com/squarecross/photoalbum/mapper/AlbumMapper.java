package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumMapper {

    public static AlbumDto convertToDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(album.getId());
        albumDto.setAlbumName(album.getName());
        albumDto.setCreatedAt(album.getCreatedAt());

        return albumDto;
    }

    public static Album convertToModel(AlbumDto albumDto) {
        Album album = new Album();
        album.setId(albumDto.getAlbumId());
        album.setName(albumDto.getAlbumName());
        album.setCreatedAt(album.getCreatedAt());

        return album;
    }

    public static List<AlbumDto> convertToList(List<Album> albums) {
        return albums.stream()
                .map(album -> AlbumMapper.convertToDto(album))
                .collect(Collectors.toList());
    }
}
