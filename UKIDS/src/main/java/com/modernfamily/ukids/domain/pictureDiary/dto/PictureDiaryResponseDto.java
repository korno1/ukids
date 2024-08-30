package com.modernfamily.ukids.domain.pictureDiary.dto;

import lombok.Data;

@Data
public class PictureDiaryResponseDto {
    private Long pictureDiaryId;
    private Long familyId;
    private String pictureUrl;
    private String content;
    private String date;
}
