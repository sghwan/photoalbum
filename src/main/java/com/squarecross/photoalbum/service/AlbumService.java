package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public AlbumDto getAlbum(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("앨범 아이디 %d로 조회 되지 않았습니다.", albumId)));
        AlbumDto albumDto = AlbumMapper.convertToDto(album);
        albumDto.setCount(photoRepository.countByAlbumId(album.getId()));

        return albumDto;
    }

    public AlbumDto getAlbumByName(String albumName) {
        Album album = albumRepository.findByName(albumName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("앨범명 %s으로 조회 되지 않았습니다.", albumName)));
        AlbumDto albumDto = AlbumMapper.convertToDto(album);
        albumDto.setCount(photoRepository.countByAlbumId(album.getId()));

        return albumDto;
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        Album savedAlbum = albumRepository.save(album);
        createDirectories(savedAlbum.getId());

        return AlbumMapper.convertToDto(savedAlbum);
    }

    private void createDirectories(Long albumId) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "\\photos\\original\\" + albumId));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "\\photos\\thumb\\" + albumId));
    }
}
