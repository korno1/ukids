package com.modernfamily.ukids.domain.growthFolder.controller;

import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderPaginationDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderRequestDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderResponseDto;
import com.modernfamily.ukids.domain.growthFolder.dto.GrowthFolderUpdateRequestDto;
import com.modernfamily.ukids.domain.growthFolder.message.SuccessMessage;
import com.modernfamily.ukids.domain.growthFolder.model.service.GrowthFolderService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/growth-folder")
@RequiredArgsConstructor
public class GrowthFolderController {

    private final GrowthFolderService growthFolderService;
    private final HttpResponseUtil httpResponseUtil;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createGrowthFolder(@RequestBody GrowthFolderRequestDto growthFolderRequestDto) {
        growthFolderService.createGrowthFolder(growthFolderRequestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_GROWTHFOLDER.getMessage());

    }

    @GetMapping("/all/{familyId}")
    public ResponseEntity<Map<String, Object>> getGrowthFolders(@PathVariable("familyId") Long familyId,
                                                                @RequestParam(value = "size", defaultValue = "5") int size,
                                                                @RequestParam(value = "page", defaultValue = "1") int page){
        GrowthFolderPaginationDto growthFolders = growthFolderService.getGrowthFolders(familyId, size, page);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, growthFolders);
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<Map<String, Object>> updateGrowthFolder(@PathVariable("folderId") Long folderId,
                                                                  @RequestBody GrowthFolderUpdateRequestDto growthFolderUpdateRequestDto){
        growthFolderUpdateRequestDto.setFolderId(folderId);
        growthFolderService.updateGrowthFolder(growthFolderUpdateRequestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_GROWTHFOLDER.getMessage());
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Map<String, Object>> deleteGrowthFolder(@PathVariable("folderId") Long folderId){
        growthFolderService.deleteGrowthFolder(folderId);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_GROWTHFOLDER.getMessage());
    }
}
