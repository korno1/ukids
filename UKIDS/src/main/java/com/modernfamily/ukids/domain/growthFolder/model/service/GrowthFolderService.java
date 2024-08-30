package com.modernfamily.ukids.domain.growthFolder.model.service;

import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderPaginationDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderRequestDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderResponseDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderUpdateRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GrowthFolderService {

    void createGrowthFolder(GrowthFolderRequestDto growthFolderRequestDto);

    GrowthFolderPaginationDto getGrowthFolders(Long familyId, int size, int page);

    void updateGrowthFolder(GrowthFolderUpdateRequestDto growthFolderUpdateRequestDto);

    void deleteGrowthFolder(Long folderId);
}
