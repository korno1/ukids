package com.modernfamily.ukids.domain.pictureDiary.dto;

import lombok.Data;

import java.util.List;

@Data
public class PictureDiaryPaginationDto {

    public PictureDiaryPaginationDto(List<PictureDiaryResponseDto> pictureDiaries, int totalPage, int size, int currentPage) {
        this.pictureDiaries = pictureDiaries;
        this.totalPage = totalPage;
        this.size = size;
        this.currentPage = currentPage;
    }

    private List<PictureDiaryResponseDto> pictureDiaries;
    private int totalPage;
    private int size;
    private int currentPage;


}
