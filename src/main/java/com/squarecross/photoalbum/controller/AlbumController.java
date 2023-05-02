package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{albumId}")
    public AlbumDto getAlbumById(@PathVariable Long albumId) {
        return albumService.getAlbum(albumId);
    }

    @GetMapping("/query")
    public AlbumDto getAlbumByQuery(@RequestParam Long albumId) {
        return albumService.getAlbum(albumId);
    }

    @PostMapping("/json_body")
    public AlbumDto getAlbumByJson(@RequestBody IdDto idDto) {
        return albumService.getAlbum(idDto.getAlbumId());
    }
}
