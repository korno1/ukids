package com.modernfamily.ukids.domain.pictureDiary.model.repository;

import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryResponseDto;
import com.modernfamily.ukids.domain.pictureDiary.entity.PictureDiary;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface CustomPictureDiaryRepository {
    List<PictureDiary> getPictureDiariesByDate(Long familyId, LocalDate date);
    List<PictureDiary> getPictureDiariesAll(Long familyId);

    void deletePictureDiary(Long pictureDiaryId);

    void updatePictureDiary(PictureDiary pictureDiary);
}
