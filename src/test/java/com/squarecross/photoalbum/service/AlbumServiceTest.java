package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.AlbumRepository;
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

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setName("새로운 앨범");
        Album savedAlbum = albumRepository.save(album);

        Album result = albumService.getAlbum(savedAlbum.getId());

        assertThat(result.getName()).isEqualTo("새로운 앨범");
    }

    @Test
    void getAlbumByName() {
        Album album = new Album();
        album.setName("새로운 앨범");
        Album savedAlbum = albumRepository.save(album);

        Album result = albumService.getAlbumByName(savedAlbum.getName());

        assertThat(result.getName()).isEqualTo("새로운 앨범");
    }

    @Test
    void getAlbumByNameThrowEntityNotFoundException() {
        Album album = new Album();
        album.setName("새로운 앨범2");
        Album savedAlbum = albumRepository.save(album);

        assertThatThrownBy(() -> {
            albumService.getAlbumByName("새로운 앨범");
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("앨범명 새로운 앨범으로 조회 되지 않았습니다.");
    }
}