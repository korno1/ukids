package com.modernfamily.ukids.domain.photo.model.repository;

import com.modernfamily.ukids.domain.album.entity.Album;

public interface PhotoRepositoryCustom {

    long deleteAllByAlbum(Album album);

}
