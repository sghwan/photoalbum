package com.squarecross.photoalbum.ssr.controller;

import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.ssr.service.AlbumService;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.ssr.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    @Autowired
    public AlbumController(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    @GetMapping("/{albumId}")
    public String getAlbumById(@PathVariable final Long albumId,
                               @RequestParam(defaultValue = "byDate") String sort,
                               @RequestParam(defaultValue = "") String keyword,
                               Model model) {
        AlbumDto album = albumService.getAlbum(albumId);
        List<PhotoDto> photos = photoService.getPhotos(albumId, keyword, sort);

        model.addAttribute("album", album);
        model.addAttribute("photos", photos);

        return "albums/albumDetail";
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        albumService.createAlbum(albumDto);
        return "redirect:/albums";
    }

    @GetMapping
    public String getAlbumList(@RequestParam(name = "keyword", defaultValue = "") final String keyword,
                               @RequestParam(name = "sort", defaultValue = "byDate") final String sort,
                               @RequestParam(name = "orderBy", defaultValue = "desc") final String orderBy,
                               Model model) {
        List<AlbumDto> albums = albumService.getAlbums(keyword, sort, orderBy);
        model.addAttribute("albums", albums);

        return "albums/albumList";
    }

    @PostMapping("/{albumId}/update")
    public String updateAlbumName(@PathVariable Long albumId, @RequestBody AlbumDto albumDto) {
        AlbumDto album = albumService.updateAlbumName(albumId, albumDto);
        return "redirect:/albums/" + album.getAlbumId();
    }

    @PostMapping("/{albumId}/delete")
    public String deleteAlbum(@PathVariable Long albumId) throws IOException {
        albumService.deleteAlbum(albumId);
        return "redirect:/albums";
    }
}
