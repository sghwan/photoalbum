package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{photoId}")
    public PhotoDto getPhoto(@PathVariable Long photoId) {
        return photoService.getPhoto(photoId);
    }

    @PostMapping
    public List<PhotoDto> uploadPhotos(@PathVariable Long albumId, @RequestParam("photos") MultipartFile[] files) throws IOException {
        photoService.validateFiles(files);
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        return photos;
    }
}
