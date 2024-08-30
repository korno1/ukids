package com.modernfamily.ukids.domain.caption.model.repository;

import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.photo.entity.Photo;


public interface CaptionRepositoryCustom {
    long deleteAllByAlbum(Album album);

    long deleteAllByPhoto(Photo photo);
}
