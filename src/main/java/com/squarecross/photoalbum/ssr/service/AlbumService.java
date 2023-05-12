package com.squarecross.photoalbum.ssr.service;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public AlbumDto getAlbum(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("앨범 아이디 %d로 조회 되지 않았습니다.", albumId)));
        AlbumDto albumDto = AlbumMapper.convertToDto(album);
        albumDto.setCount(photoRepository.countByAlbumId(album.getId()));

        return albumDto;
    }

    public AlbumDto getAlbumByName(String albumName) {
        Album album = albumRepository.findByName(albumName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("앨범명 %s으로 조회 되지 않았습니다.", albumName)));
        AlbumDto albumDto = AlbumMapper.convertToDto(album);
        albumDto.setCount(photoRepository.countByAlbumId(album.getId()));

        return albumDto;
    }

    @Transactional
    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        Album savedAlbum = albumRepository.save(album);
        createDirectories(savedAlbum.getId());

        return AlbumMapper.convertToDto(savedAlbum);
    }

    public List<AlbumDto> getAlbums(String keyword, String sort, String orderBy) {
        List<Album> result = null;

        if (sort.equals("byDate")) {
            if (orderBy.equals("desc"))
                result = albumRepository.findAllByAlbumNameContainingOrderByCreatedAtDesc(keyword);
            else
                result = albumRepository.findAllByAlbumNameContainingOrderByCreatedAtAsc(keyword);
        } else if (sort.equals("byName")) {
            if (orderBy.equals("desc"))
                result = albumRepository.findAllByAlbumNameContainingOrderByAlbumNameDesc(keyword);
            else
                result = albumRepository.findAllByAlbumNameContainingOrderByAlbumNameAsc(keyword);
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다");
        }

        List<AlbumDto> albumDtos = AlbumMapper.convertToList(result);

        for (AlbumDto albumDto : albumDtos) {
            List<String> thumbUrls = photoRepository.findTop4ByAlbum_IdOrderByUploadedAt(albumDto.getAlbumId())
                    .stream()
                    .map(photo -> photo.getThumbUrl())
                    .collect(Collectors.toList());
            int count = photoRepository.countByAlbumId(albumDto.getAlbumId());

            albumDto.setThumbUrls(thumbUrls);
            albumDto.setCount(count);
        }

        return albumDtos;
    }

    @Transactional
    public AlbumDto updateAlbumName(Long albumId, AlbumDto albumDto) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("앨범 아이디 %d로 조회 되지 않았습니다.", albumId)));
        album.setName(albumDto.getAlbumName());

        return AlbumMapper.convertToDto(album);
    }

    @Transactional
    public void deleteAlbum(Long albumId) throws IOException {
        deleteDirectories(albumId);
        albumRepository.deleteById(albumId);
    }

    private void createDirectories(Long albumId) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "\\photos\\original\\" + albumId));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "\\photos\\thumb\\" + albumId));
    }

    private void deleteDirectories(Long albumId) throws IOException {
        deleteDirectory(Paths.get(Constants.PATH_PREFIX + "\\photos\\original\\" + albumId));
        deleteDirectory(Paths.get(Constants.PATH_PREFIX + "\\photos\\thumb\\" + albumId));
    }

    private void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
