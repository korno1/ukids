package com.modernfamily.ukids.domain.pictureDiary.controller;

import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryPaginationDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryRequestDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryResponseDto;
import com.modernfamily.ukids.domain.pictureDiary.dto.PictureDiaryUpdateDto;
import com.modernfamily.ukids.domain.pictureDiary.message.SuccessMessage;
import com.modernfamily.ukids.domain.pictureDiary.model.service.PictureDiaryService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/picture-diary")
@RequiredArgsConstructor
public class PictureDiaryController {
    private final PictureDiaryService pictureDiaryService;
    private final HttpResponseUtil httpResponseUtil;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPictureDiary(@ModelAttribute PictureDiaryRequestDto pictureDiaryRequestDto){
        pictureDiaryService.createPictureDiary(pictureDiaryRequestDto);

        return httpResponseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_PICTUREDIARY.getMessage());
    }

    //세부조회
    @GetMapping("/details/{pictureDiaryId}")
    public ResponseEntity<Map<String, Object>> getPictureDiary(@PathVariable("pictureDiaryId") Long pictureDiaryId){
        PictureDiaryResponseDto pictureDiaryResponseDto = pictureDiaryService.getPictureDiary(pictureDiaryId);

        return httpResponseUtil.createResponse(HttpMethodCode.GET, pictureDiaryResponseDto);
    }

    // 날짜로 조회
    @GetMapping("/{familyId}")
    public ResponseEntity<Map<String, Object>> getPictureDiariesByDate(@PathVariable("familyId") Long familyId,
                                                                       @RequestParam("date") LocalDate date,
                                                                       @RequestParam(value = "size", defaultValue = "5") int size,
                                                                       @RequestParam(value = "page", defaultValue = "1") int page){

        PictureDiaryPaginationDto pictureDiaryPaginationDto = pictureDiaryService.getPictureDiariesByDate(familyId, date, size, page);

        return httpResponseUtil.createResponse(HttpMethodCode.GET, pictureDiaryPaginationDto);
    }
    // 전체 조회
    @GetMapping("/all/{familyId}")
    public ResponseEntity<Map<String, Object>> getPictureDiariesAll(@PathVariable("familyId") Long familyId,
                                                                    @RequestParam(value = "size", defaultValue = "5") int size,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page){
        PictureDiaryPaginationDto pictureDiaryPaginationDto = pictureDiaryService.getPictureDiariesAll(familyId, size, page);

        return httpResponseUtil.createResponse(HttpMethodCode.GET, pictureDiaryPaginationDto);
    }

    @DeleteMapping("/{pictureDiaryId}")
    public ResponseEntity<Map<String, Object>> deletePictureDiary(@PathVariable("pictureDiaryId") Long pictureDiaryId) throws IOException {
        pictureDiaryService.deletePictureDiary(pictureDiaryId);
        return httpResponseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_PICTUREDIARY.getMessage());
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updatePictureDiary(@ModelAttribute PictureDiaryUpdateDto pictureDiaryUpdateDto) throws IOException {
        pictureDiaryService.updatePictureDiary(pictureDiaryUpdateDto);

        return httpResponseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_PICTUREDIARY.getMessage());
    }


}
