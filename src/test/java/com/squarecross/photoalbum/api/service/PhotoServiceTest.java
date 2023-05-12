package com.squarecross.photoalbum.api.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDetailDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.dto.PhotoIdsDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoServiceTest {
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoService photoService;

    @Test
    void getPhoto() {
        Photo photo = new Photo();
        photo.setFileName("새폴더");
        photo.setFileSize(12345667);
        photo.setThumbUrl("thumbUrl");
        photo.setOriginalUrl("originalUrl");
        Photo savedPhoto = photoRepository.save(photo);

        PhotoDetailDto result = photoService.getPhoto(savedPhoto.getId());
        assertThat(result.getFileName()).isEqualTo("새폴더");
        assertThat(result.getFileSize()).isEqualTo(12345667);
    }

    @Test
    void getPhotos() throws InterruptedException {
        Album album = new Album();
        album.setName("album1");
        Album savedAlbum = albumRepository.save(album);

        Photo photo = new Photo();
        photo.setFileName("새폴더");
        photo.setThumbUrl("thumbUrl");
        photo.setAlbum(album);
        Photo savedPhoto = photoRepository.save(photo);

        Thread.sleep(1000);

        Photo photo2 = new Photo();
        photo2.setFileName("새폴더2");
        photo2.setThumbUrl("thumbUrl2");
        photo2.setAlbum(album);
        Photo savedPhoto2 = photoRepository.save(photo2);

        List<PhotoDto> photos = photoService.getPhotos(savedAlbum.getId(), "", "byDate");
        assertThat(photos.size()).isEqualTo(2);
        assertThat(photos.get(0).getFileName()).isEqualTo("새폴더2");
        assertThat(photos.get(1).getFileName()).isEqualTo("새폴더");
    }

    @Test
    void deletePhotos() throws IOException {
        Album album = new Album();
        album.setName("album1");
        Album savedAlbum = albumRepository.save(album);

        Photo photo = new Photo();
        photo.setFileName("새폴더");
        photo.setThumbUrl("thumbUrl");
        photo.setAlbum(album);
        Photo savedPhoto = photoRepository.save(photo);

        Photo photo2 = new Photo();
        photo2.setFileName("새폴더2");
        photo2.setThumbUrl("thumbUrl2");
        photo2.setAlbum(album);
        Photo savedPhoto2 = photoRepository.save(photo2);

        List<PhotoDto> photos = photoService.getPhotos(savedAlbum.getId(), "", "byDate");
        assertThat(photos.size()).isEqualTo(2);
        assertThat(photos.get(0).getFileName()).isEqualTo("새폴더2");
        assertThat(photos.get(1).getFileName()).isEqualTo("새폴더");

        PhotoIdsDto photoIdsDto = new PhotoIdsDto();
        List<Long> photoIds = new ArrayList<>();
        photoIds.add(savedPhoto.getId());
        photoIds.add(savedPhoto2.getId());
        photoIdsDto.setPhotoIds(photoIds);

        photoService.deletePhotos(photoIdsDto);
        List<PhotoDto> result = photoService.getPhotos(savedAlbum.getId(), "", "byDate");
        assertThat(result.size()).isEqualTo(0);
    }
}