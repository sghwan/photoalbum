package com.squarecross.photoalbum.api.controller;

import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.api.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//@RestController
@RequestMapping("/api/albums")
@CrossOrigin
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{albumId}")
    public AlbumDto getAlbumById(@PathVariable final Long albumId) {
        return albumService.getAlbum(albumId);
    }

    @GetMapping("/query")
    public AlbumDto getAlbumByQuery(@RequestParam final Long albumId) {
        return albumService.getAlbum(albumId);
    }

    @PostMapping("/json_body")
    public AlbumDto getAlbumByJson(@RequestBody final AlbumDto albumDto) {
        return albumService.getAlbum(albumDto.getAlbumId());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public AlbumDto createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        return albumService.createAlbum(albumDto);
    }

    @GetMapping
    public List<AlbumDto> getAlbumList(@RequestParam(name = "keyword", defaultValue = "") final String keyword,
                                       @RequestParam(name = "sort", defaultValue = "byDate") final String sort,
                                       @RequestParam(name = "orderBy", defaultValue = "desc") final String orderBy) {
        return albumService.getAlbums(keyword, sort, orderBy);
    }

    @PatchMapping("/{albumId}")
    public AlbumDto updateAlbumName(@PathVariable Long albumId, @RequestBody AlbumDto albumDto) {
        return albumService.updateAlbumName(albumId, albumDto);
    }

    @DeleteMapping("/{albumId}")
    public void deleteAlbum(@PathVariable Long albumId) throws IOException {
        albumService.deleteAlbum(albumId);
    }
}
