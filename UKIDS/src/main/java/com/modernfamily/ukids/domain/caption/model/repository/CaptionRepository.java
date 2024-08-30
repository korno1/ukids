package com.modernfamily.ukids.domain.caption.model.repository;

import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.caption.entity.Caption;
import com.modernfamily.ukids.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptionRepository extends JpaRepository<Caption, Long>, CaptionRepositoryCustom {
    Optional<Caption> findByCaptionId(Long captionId);
    Optional<Caption> findByPhoto_PhotoId(Long photoId);
    Page<Caption> findAllByPhoto_Album_AlbumIdOrderByCreateTimeDesc(Long albumId, Pageable pageable);

    @Override
    long deleteAllByAlbum(Album album);

    @Override
    long deleteAllByPhoto(Photo photo);
}
