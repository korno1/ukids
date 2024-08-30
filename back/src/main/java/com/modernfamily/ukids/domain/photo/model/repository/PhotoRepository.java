package com.modernfamily.ukids.domain.photo.model.repository;

import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom {

    Optional<Photo> findByPhotoId(Long id);

    Page<Photo> findAllByAlbum_AlbumIdOrderByCreateTimeDesc(Long AlbumId, Pageable pageable);

    @Override
    long deleteAllByAlbum(Album album);


    @Modifying
    @Transactional
    @Query(value = "update Photo " +
            "set isDelete = true " +
            "where photoS3Name = :photoS3Name")
    void deleteUploadedPhoto(String photoS3Name);
}
