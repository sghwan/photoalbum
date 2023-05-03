package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.Constants;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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

    @Test
    void createAlbum() throws IOException {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("새폴더");
        AlbumDto createdAlbumDto = albumService.createAlbum(albumDto);

        File origin = new File(Constants.PATH_PREFIX + "\\photos\\original\\" + createdAlbumDto.getAlbumId());
        File thumb = new File(Constants.PATH_PREFIX + "\\photos\\thumb\\" + createdAlbumDto.getAlbumId());

        boolean mkdir1 = origin.mkdir();
        boolean mkdir2 = thumb.mkdir();
        assertThat(mkdir1).isFalse();
        assertThat(mkdir2).isFalse();

        boolean delete1 = origin.delete();
        boolean delete2 = thumb.delete();
        assertThat(delete1).isTrue();
        assertThat(delete2).isTrue();
    }

    @Test
    void getAlbums() throws InterruptedException {
        List<AlbumDto> result = albumService.getAlbums("", "byDate", "asc");
        for (AlbumDto albumDto : result) {
            System.out.println("albumDto.getAlbumId() = " + albumDto.getAlbumId());
            System.out.println("albumDto.getAlbumName() = " + albumDto.getAlbumName());
            System.out.println("albumDto.getCount() = " + albumDto.getCount());
            for (String thumbUrl : albumDto.getThumbUrls()) {
                System.out.println("thumbUrl = " + thumbUrl);
            }
        }
    }
}