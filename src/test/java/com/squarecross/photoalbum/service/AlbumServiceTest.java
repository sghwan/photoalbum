package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    AlbumService albumService;

    @Autowired
    PhotoRepository photoRepository;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setName("새로운 앨범");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto result = albumService.getAlbum(savedAlbum.getId());

        assertThat(result.getAlbumName()).isEqualTo("새로운 앨범");
    }

    @Test
    void getAlbumByName() {
        Album album = new Album();
        album.setName("새로운 앨범");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto result = albumService.getAlbumByName(savedAlbum.getName());

        assertThat(result.getAlbumName()).isEqualTo("새로운 앨범");
    }

    @Test
    void getAlbumByNameThrowEntityNotFoundException() {
        Album album = new Album();
        album.setName("새로운 앨범2");
        albumRepository.save(album);

        assertThatThrownBy(() -> {
            albumService.getAlbumByName("새로운 앨범");
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("앨범명 새로운 앨범으로 조회 되지 않았습니다.");
    }

    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setName("새로운 앨범");
        Album savedAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("새로운 사진1");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("새로운 사진2");
        photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        int result = photoRepository.countByAlbumId(savedAlbum.getId());

        assertThat(result).isEqualTo(2);
    }
}