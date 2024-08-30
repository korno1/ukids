package com.modernfamily.ukids.domain.growthRecord.controller;

import com.modernfamily.ukids.domain.growthRecord.dto.*;
import com.modernfamily.ukids.domain.growthRecord.message.SuccessMessage;
import com.modernfamily.ukids.domain.growthRecord.model.service.GrowthRecordService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/growth-record")
@RequiredArgsConstructor
public class GrowthRecordController {

    private final GrowthRecordService growthRecordService;
    private final HttpResponseUtil httpResponseUtil;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createGrowthRecord(@ModelAttribute GrowthRecordRequestDto growthRecordRequestDto){

        growthRecordService.createGrowthRecord(growthRecordRequestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_GROWTHRECORD.getMessage());
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<Map<String, Object>> updateGrowthRecord(@ModelAttribute GrowthRecordUpdateDto growthRecordUpdateDto,
                                                                  @PathVariable("recordId") Long recordId) throws IOException{
        growthRecordUpdateDto.setRecordId(recordId);
        growthRecordService.updateGrowthRecord(growthRecordUpdateDto);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_UPDATE_GROWTHRECORD.getMessage());
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<Map<String,Object>> getGrowthRecord(@PathVariable("recordId") Long recordId){
        GrowthRecordResponseDto growthRecordResponseDto = growthRecordService.getGrowthRecord(recordId);

        return httpResponseUtil.createResponse(HttpMethodCode.GET, growthRecordResponseDto);
    }

    @GetMapping("/all/{folderId}")
    public ResponseEntity<Map<String,Object>> getGrowthRecords(@PathVariable("folderId") Long folderId,
                                                               @RequestParam(value = "size", defaultValue = "5") int size,
                                                               @RequestParam(value = "page", defaultValue = "1") int page){
        GrowthRecordPaginationDto growthRecordPaginationDto = growthRecordService.getGrowthRecords(folderId, size, page);

        return httpResponseUtil.createResponse(HttpMethodCode.GET, growthRecordPaginationDto);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Map<String,Object>> deleteGrowthRecord(@PathVariable("recordId") Long recordId) throws IOException {
        growthRecordService.deleteGrowthRecord(recordId);

        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_GROWTHRECORD);
    }

}
