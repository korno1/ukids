package com.modernfamily.ukids.domain.growthFolder.dto;

import lombok.Data;

import java.util.List;

@Data
public class GrowthFolderPaginationDto {

    public GrowthFolderPaginationDto(List<GrowthFolderResponseDto> growthFolders, int totalPages, int size, int currentPage) {
        this.growthFolders = growthFolders;
        this.totalPages = totalPages;
        this.size = size;
        this.currentPage = currentPage;
    }

    List<GrowthFolderResponseDto> growthFolders;
    private int totalPages;
    private int size;
    private int currentPage;
}
