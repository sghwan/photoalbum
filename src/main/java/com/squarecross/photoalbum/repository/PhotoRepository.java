package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select count(p) from Photo p where p.album.id = :albumId")
    int countByAlbumId(@Param("albumId") Long albumId);

    List<Photo> findTop4ByAlbum_IdOrderByUploadedAt(Long id);
}
