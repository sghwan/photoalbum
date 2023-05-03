package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoServiceTest {

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

        PhotoDto result = photoService.getPhoto(savedPhoto.getId());
        assertThat(result.getFileName()).isEqualTo("새폴더");
        assertThat(result.getFileSize()).isEqualTo(12345667);
    }
}