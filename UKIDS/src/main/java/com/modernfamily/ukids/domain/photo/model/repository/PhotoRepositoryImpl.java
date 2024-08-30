package com.modernfamily.ukids.domain.photo.model.repository;


import com.modernfamily.ukids.domain.album.entity.Album;
import com.modernfamily.ukids.domain.album.entity.QAlbum;
import com.modernfamily.ukids.domain.photo.entity.QPhoto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private QPhoto photo = QPhoto.photo;

    @Override
    public long deleteAllByAlbum(Album album){
        return jpaQueryFactory
                .update(photo)
                .set(photo.isDelete, true)
                .where(photo.album.eq(album))
                .execute();
    }
}
