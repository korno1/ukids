package com.modernfamily.ukids.domain.pictureDiary.model.service;

import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryPaginationDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryRequestDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryResponseDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryUpdateDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PictureDiaryService {

    void createPictureDiary(PictureDiaryRequestDto pictureDiaryRequestDto);

    PictureDiaryResponseDto getPictureDiary(Long pictureDiaryId);

    PictureDiaryPaginationDto getPictureDiariesByDate(Long familyId, LocalDate date, int size, int page);
    PictureDiaryPaginationDto getPictureDiariesAll(Long familyId, int size, int page);

    void deletePictureDiary(Long pictureDiaryId) throws IOException;

    void updatePictureDiary(PictureDiaryUpdateDto pictureDiaryUpdateDto) throws IOException;
}
