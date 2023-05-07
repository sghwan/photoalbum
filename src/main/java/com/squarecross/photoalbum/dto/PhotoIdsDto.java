package com.squarecross.photoalbum.dto;

import java.util.ArrayList;
import java.util.List;

public class PhotoIdsDto {
    private List<Long> photoIds = new ArrayList<>();

    public List<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Long> photoIds) {
        this.photoIds = photoIds;
    }
}
