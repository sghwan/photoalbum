package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.PhotoDetailDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.dto.PhotoIdsDto;
import com.squarecross.photoalbum.dto.PhotoMoveDto;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{photoId}")
    public PhotoDetailDto getPhoto(@PathVariable Long photoId) {
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

    @GetMapping("/download")
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse response) {
        try {
            if (photoIds.length == 1) {
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();
            } else {
                response.setContentType("application/zip");
                ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
                FileInputStream fis = null;
                for (Long photoId : photoIds) {
                    File file = photoService.getImageFile(photoId);
                    zipOut.putNextEntry(new ZipEntry(file.getName()));
                    fis = new FileInputStream(file);
                    StreamUtils.copy(fis, zipOut);
                    fis.close();
                    zipOut.closeEntry();
                }
                zipOut.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public List<PhotoDto> getPhotos(@PathVariable Long albumId,
                                    @RequestParam(defaultValue = "byDate") String sort,
                                    @RequestParam(defaultValue = "") String keyword) {
        return photoService.getPhotos(albumId, keyword, sort);
    }

    @DeleteMapping
    public List<PhotoDto> deletePhotos(@PathVariable Long albumId,
                                       @RequestParam(defaultValue = "byDate") String sort,
                                       @RequestParam(defaultValue = "") String keyword,
                                       @RequestBody PhotoIdsDto photoIdsDto) throws IOException {
        photoService.deletePhotos(photoIdsDto);
        return photoService.getPhotos(albumId, keyword, sort);
    }

    @PatchMapping("/move")
    public List<PhotoDto> movePhotos(@RequestBody PhotoMoveDto photoMoveDto) throws IOException {
        photoService.movePhotos(photoMoveDto);
        return photoService.getPhotos(photoMoveDto.getFromAlbumId(), "", "byDate");
    }
}
