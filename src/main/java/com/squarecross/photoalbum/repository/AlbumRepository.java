package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByName(String name);

    @Query("select a from Album a where a.user.id = :userId and a.name like %:keyword% order by a.createdAt desc")
    List<Album> findAllByAlbumNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("select a from Album a where a.user.id = :userId and a.name like %:keyword% order by a.createdAt")
    List<Album> findAllByAlbumNameContainingOrderByCreatedAtAsc(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("select a from Album a where a.user.id = :userId and a.name like %:keyword% order by a.name desc")
    List<Album> findAllByAlbumNameContainingOrderByAlbumNameDesc(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("select a from Album a where a.user.id = :userId and a.name like %:keyword% order by a.name")
    List<Album> findAllByAlbumNameContainingOrderByAlbumNameAsc(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("select a from Album a where a.name like %:keyword% order by a.createdAt desc")
    List<Album> findAllByAlbumNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    @Query("select a from Album a where a.name like %:keyword% order by a.createdAt")
    List<Album> findAllByAlbumNameContainingOrderByCreatedAtAsc(@Param("keyword") String keyword);

    @Query("select a from Album a where a.name like %:keyword% order by a.name desc")
    List<Album> findAllByAlbumNameContainingOrderByAlbumNameDesc(@Param("keyword") String keyword);

    @Query("select a from Album a where a.name like %:keyword% order by a.name")
    List<Album> findAllByAlbumNameContainingOrderByAlbumNameAsc(@Param("keyword") String keyword);
}
