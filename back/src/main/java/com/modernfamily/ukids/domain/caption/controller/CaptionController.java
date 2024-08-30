package com.modernfamily.ukids.domain.caption.controller;

import com.modernfamily.ukids.domain.caption.dto.request.CaptionCreateRequestDto;
import com.modernfamily.ukids.domain.caption.dto.request.CaptionUpdateRequestDto;
import com.modernfamily.ukids.domain.caption.message.SuccessMessage;
import com.modernfamily.ukids.domain.caption.model.service.CaptionService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/caption")
@RequiredArgsConstructor
public class CaptionController {

    private final HttpResponseUtil responseUtil;
    private final CaptionService captionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCaption(@RequestBody CaptionCreateRequestDto requestDto) {
        captionService.createCaption(requestDto);

        return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_CAPTION.getMessage());
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateCaption(@RequestBody CaptionUpdateRequestDto requestDto) {
        captionService.updateCaption(requestDto);

        return responseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_CAPTION.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCaption(@PathVariable("id") Long captionId) {
        captionService.deleteCaption(captionId);

        return responseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_CAPTION.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCaption(@PathVariable("id") Long photoId) {

        return responseUtil.createResponse(HttpMethodCode.GET, captionService.getCaption(photoId));
    }
}
