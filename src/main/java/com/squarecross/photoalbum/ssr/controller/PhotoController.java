package com.squarecross.photoalbum.ssr.controller;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.ssr.service.AlbumService;
import com.squarecross.photoalbum.ssr.service.PhotoService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final AlbumService albumService;

    @Autowired
    public PhotoController(PhotoService photoService, AlbumService albumService) {
        this.photoService = photoService;
        this.albumService = albumService;
    }

    @GetMapping("/{photoId}")
    public String getPhoto(@PathVariable Long albumId, @PathVariable Long photoId, Model model) {
        PhotoDetailDto photo = photoService.getPhoto(photoId);
        List<AlbumDto> albums = albumService.getAlbums("", "byDate", "desc");
        model.addAttribute("photo", photo);
        model.addAttribute("albumId", albumId);
        model.addAttribute("albums", albums);
        model.addAttribute("photoIdsDto", new PhotoIdsDto());

        return "photos/photoDetail";
    }

    @GetMapping("/upload")
    public String uploadForm(@PathVariable Long albumId, Model model) throws IOException {
        model.addAttribute("albumId", albumId);
        return "photos/photoUpload";
    }

    @PostMapping("/upload")
    public String uploadPhotos(@PathVariable Long albumId, @RequestParam("photos") MultipartFile[] files, Model model) throws IOException {
        photoService.validateFiles(files);
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        model.addAttribute("photos", photos);

        return "redirect:/albums/" + albumId;
    }

    @GetMapping("/download")
    public void downloadPhotos(@PathVariable Long albumId, @RequestParam("photoIds") Long[] photoIds, HttpServletResponse response) {
        try {
            if (photoIds.length == 1) {
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                FileInputStream fileInputStream = new FileInputStream(file);
                String encode = UriUtils.encode(file.getName(), StandardCharsets.UTF_8);
                response.addHeader("Content-Disposition", "attachment; filename=\"" + encode + "\"");
                IOUtils.copy(fileInputStream, outputStream);
                fileInputStream.close(); //파일을 닫아 줘야 다른 프로세스에서 접근이 끊겨 다시 사용할 수 있다. 다운로드 이후 삭제나 이동시 접근을 못했던 이유가 이 코드가 없어서 였다.
                outputStream.close();
            } else {
                AlbumDto album = albumService.getAlbum(albumId);
                response.setContentType("application/zip");
                ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
                String encode = UriUtils.encode(album.getAlbumName(), StandardCharsets.UTF_8);
                //확장자를 잘 지정해줘야 바로 zip파일로 다운 받을 수 있다.
                response.addHeader("Content-Disposition", "attachment; filename=\"" + encode + ".zip" + "\"");
                FileInputStream fis = null;
                for (Long photoId : photoIds) {
                    File file = photoService.getImageFile(photoId);
                    zipOut.putNextEntry(new ZipEntry(file.getName()));
                    fis = new FileInputStream(file);
                    StreamUtils.copy(fis, zipOut);
                    fis.close(); //여러장을 다운받을 경우 애초에 파일 인풋 스트림을 릴리즈 해주었기 때문에 단일로 받을때 일어났던 코드오류가 없었다.
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

    @PostMapping("/delete")
    public String deletePhotos(@PathVariable Long albumId,
                               @ModelAttribute PhotoIdsDto photoIdsDto) throws IOException {
        photoService.deletePhotos(photoIdsDto);
        return "redirect:/albums/" + albumId;
    }

    @PostMapping("/move")
    public String movePhotos(@RequestBody PhotoMoveDto photoMoveDto) throws IOException {
        photoService.movePhotos(photoMoveDto);
        return "redirect:/albums/" + photoMoveDto.getFromAlbumId();
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable Long albumId, @PathVariable String filename) throws IOException {
        return new UrlResource("file:" + Constants.PATH_PREFIX + "\\photos\\thumb\\" + albumId + "\\" + filename);
    }

    @ResponseBody
    @GetMapping("/images/{filename}/original")
    public Resource downloadOriginImage(@PathVariable Long albumId, @PathVariable String filename) throws IOException {
        return new UrlResource("file:" + Constants.PATH_PREFIX + "\\photos\\original\\" + albumId + "\\" + filename);
    }
}
