package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDetailDto;
import com.squarecross.photoalbum.dto.PhotoDto;

public class PhotoMapper {

    public static PhotoDetailDto convertToDetailDto(Photo photo) {
        PhotoDetailDto photoDetailDto = new PhotoDetailDto();
        photoDetailDto.setPhotoId(photo.getId());
        photoDetailDto.setFileName(photo.getFileName());
        photoDetailDto.setFileSize(photo.getFileSize());
        photoDetailDto.setOriginalUrl(photo.getOriginalUrl());
        photoDetailDto.setUploadedAt(photo.getUploadedAt());

        return photoDetailDto;
    }

    public static PhotoDto convertToDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setPhotoId(photo.getId());
        photoDto.setFileName(photo.getFileName());
        photoDto.setThumbUrl(photo.getThumbUrl());
        photoDto.setUploadedAt(photo.getUploadedAt());

        return photoDto;
    }
}
