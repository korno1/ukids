package com.modernfamily.ukids.domain.growthRecord.dto;

import lombok.Data;

import java.util.List;

@Data
public class GrowthRecordPaginationDto {

    public GrowthRecordPaginationDto(List<GrowthRecordResponseDto> growthRecords, int totalPage, int size, int currentPage) {
        this.growthRecords = growthRecords;
        this.totalPage = totalPage;
        this.size = size;
        this.currentPage = currentPage;
    }

    private List<GrowthRecordResponseDto> growthRecords;
    private int totalPage;
    private int size;
    private int currentPage;
}
