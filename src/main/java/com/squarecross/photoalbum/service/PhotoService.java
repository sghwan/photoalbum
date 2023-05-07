package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDetailDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.dto.PhotoIdsDto;
import com.squarecross.photoalbum.dto.PhotoMoveDto;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;

    private final String original_path = Constants.PATH_PREFIX + "\\photos\\original";
    private final String thumb_path = Constants.PATH_PREFIX + "\\photos\\thumb";

    @Autowired
    public PhotoService(PhotoRepository photoRepository, AlbumRepository albumRepository) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
    }

    public PhotoDetailDto getPhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("사진 아이디 %d로 조회 되지 않았습니다.", photoId)));

        return PhotoMapper.convertToDetailDto(photo);
    }

    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범이 존재하지 않습니다."));

        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        fileName = getNextFileName(fileName, albumId);
        saveFile(file, albumId, fileName);

        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setFileSize(size);
        photo.setOriginalUrl("\\photos\\original\\" + albumId + "\\" + fileName);
        photo.setThumbUrl("\\photos\\thumb\\" + albumId + "\\" + fileName);
        photo.setAlbum(album);
        Photo savedPhoto = photoRepository.save(photo);

        return PhotoMapper.convertToDto(savedPhoto);
    }

    private String getNextFileName(String fileName, Long albumId) {
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName);
        String ext = StringUtils.getFilenameExtension(fileName);

        Optional<Photo> res = photoRepository.findByFileNameAndAlbum_Id(fileName, albumId);

        int count = 2;
        while (res.isPresent()) {
            fileName = String.format("%s (%d).%s", fileNameNoExt, count, ext);
            res = photoRepository.findByFileNameAndAlbum_Id(fileName, albumId);
            count++;
        }

        return fileName;
    }

    private void saveFile(MultipartFile file, Long albumId, String fileName) {
        try {
            String filePath = albumId + "\\" + fileName;
            Files.copy(file.getInputStream(), Paths.get(original_path + "\\" + filePath));

            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);
            File thumbFile = new File(thumb_path + "\\" + filePath);
            String ext = StringUtils.getFilenameExtension(fileName);
            if (ext == null) {
                throw new IllegalArgumentException("No extension");
            }
            ImageIO.write(thumbImg, ext, thumbFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void validateFiles(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            if (ImageIO.read(file.getInputStream()) == null)
                throw new IllegalArgumentException("이미지 파일만 올려주세요");
            if (!Constants.EXT_LIST.contains(StringUtils.getFilenameExtension(file.getOriginalFilename())))
                throw new IllegalArgumentException("이미지 파일만 올려주세요");
        }
    }

    public File getImageFile(Long photoId) throws IOException {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("사진이 존재하지 않습니다."));
        String fullPath = Constants.PATH_PREFIX + photo.getOriginalUrl();
        return new File(fullPath);
    }

    public List<PhotoDto> getPhotos(Long albumId, String keyword, String sort) {
        List<Photo> result = null;

        if (sort.equals("byDate")) {
            result = photoRepository.findAllByAlbum_IdAndOrderByUploadedAt(albumId, keyword);
        } else {
            result = photoRepository.findAllByAlbum_IdAndOrderByFileName(albumId, keyword);

        }

        return result.stream().map(photo -> PhotoMapper.convertToDto(photo))
                .collect(Collectors.toList());
    }

    public void deletePhotos(PhotoIdsDto photoIdsDto) throws IOException {
        for (Photo photo : photoRepository.findAllById(photoIdsDto.getPhotoIds())) {
            Files.deleteIfExists(Paths.get(Constants.PATH_PREFIX + photo.getOriginalUrl()));
            Files.deleteIfExists(Paths.get(Constants.PATH_PREFIX + photo.getThumbUrl()));
        }

        photoRepository.deleteAllByIdInBatch(photoIdsDto.getPhotoIds());
    }

    @Transactional
    public void movePhotos(PhotoMoveDto photoMoveDto) throws IOException {
        Album album = albumRepository.findById(photoMoveDto.getToAlbumId())
                .orElseThrow(() -> new EntityNotFoundException("이동할 앨범이 존재하지 않습니다."));

        List<Photo> photos = photoRepository.findAllById(photoMoveDto.getPhotoIds());

        for (Photo photo : photos) {
            String originalUrl = photo.getOriginalUrl();
            String thumbUrl = photo.getThumbUrl();
            String fileName = getNextFileName(photo.getFileName(), photoMoveDto.getToAlbumId());

            String srcOriginalUrl = Constants.PATH_PREFIX + originalUrl;
            String srcThumbUrl = Constants.PATH_PREFIX + thumbUrl;
            String dstOriginalUrl = Constants.PATH_PREFIX + "\\photos\\original\\" + photoMoveDto.getToAlbumId() + "\\" + fileName;
            String dstThumbUrl = Constants.PATH_PREFIX + "\\photos\\thumb\\" + photoMoveDto.getToAlbumId() + "\\" + fileName;

            Files.move(Paths.get(srcOriginalUrl), Paths.get(dstOriginalUrl));
            Files.move(Paths.get(srcThumbUrl), Paths.get(dstThumbUrl));

            photo.setAlbum(album);
            photo.setOriginalUrl("\\photos\\original\\" + photoMoveDto.getToAlbumId() + "\\" + fileName);
            photo.setThumbUrl("\\photos\\thumb\\" + photoMoveDto.getToAlbumId() + "\\" + fileName);
        }
    }
}
