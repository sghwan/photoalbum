package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select count(p) from Photo p where p.album.id = :albumId")
    int countByAlbumId(@Param("albumId") Long albumId);

    List<Photo> findTop4ByAlbum_IdOrderByUploadedAt(Long id);

    Optional<Photo> findByFileNameAndAlbum_Id(String fileName, Long albumId);

    @Query("select p from Photo p where p.album.id = :albumId and p.fileName like %:keyword% order by p.uploadedAt desc")
    List<Photo> findAllByAlbum_IdAndOrderByUploadedAt(@Param("albumId") Long albumId, @Param("keyword") String keyword);

    @Query("select p from Photo p where p.album.id = :albumId and p.fileName like %:keyword% order by p.fileName")
    List<Photo> findAllByAlbum_IdAndOrderByFileName(@Param("albumId") Long albumId, @Param("keyword") String keyword);
}
