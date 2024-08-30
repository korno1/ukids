package com.modernfamily.ukids.domain.pictureDiary.model.repository;

import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryResponseDto;
import com.modernfamily.ukids.domain.pictureDiary.entity.PictureDiary;
import com.modernfamily.ukids.domain.pictureDiary.entity.QPictureDiary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPictureDiaryRepositoryImpl implements CustomPictureDiaryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PictureDiary> getPictureDiariesByDate(Long familyId, LocalDate date) {
        QPictureDiary pictureDiary = QPictureDiary.pictureDiary;
        return jpaQueryFactory
                .selectFrom(pictureDiary)
                .where(pictureDiary.date.eq(date),
                        pictureDiary.family.familyId.eq(familyId))
                .fetch();
    }
    @Override
    public List<PictureDiary> getPictureDiariesAll(Long familyId) {
        QPictureDiary pictureDiary = QPictureDiary.pictureDiary;
        return jpaQueryFactory
                .selectFrom(pictureDiary)
                .where(pictureDiary.family.familyId.eq(familyId))
                .fetch();
    }

    @Transactional
    @Override
    public void deletePictureDiary(Long pictureDiaryId) {
        QPictureDiary pictureDiary = QPictureDiary.pictureDiary;
        jpaQueryFactory
                .update(pictureDiary)
                .set(pictureDiary.isDelete, true)
                .where(pictureDiary.pictureDiaryId.eq(pictureDiaryId))
                .execute();
    }

    @Transactional
    @Override
    public void updatePictureDiary(PictureDiary pictureDiaryInfo) {
        QPictureDiary pictureDiary = QPictureDiary.pictureDiary;
        jpaQueryFactory
                .update(pictureDiary)
                .set(pictureDiary.pictureUrl, pictureDiaryInfo.getPictureUrl())
                .set(pictureDiary.imageName, pictureDiaryInfo.getImageName())
                .set(pictureDiary.content, pictureDiaryInfo.getContent())
                .set(pictureDiary.date, pictureDiaryInfo.getDate())
                .where(pictureDiary.pictureDiaryId.eq(pictureDiaryInfo.getPictureDiaryId()))
                .execute();

    }

}
