package com.modernfamily.ukids.domain.letter.controller;

import com.modernfamily.ukids.domain.letter.dto.request.LetterCreateRequestDto;
import com.modernfamily.ukids.domain.letter.entity.Letter;
import com.modernfamily.ukids.domain.letter.message.SuccessMessage;
import com.modernfamily.ukids.domain.letter.model.service.LetterService;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;
    private final HttpResponseUtil responseUtil;

    // 편지 작성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createLetter(@RequestBody LetterCreateRequestDto letterDto) {
        letterService.createLetter(letterDto);

        return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_LETTER.getMessage());
    }

    // 편지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteLetter(@PathVariable("id") Long letterId) {
        letterService.deleteLetter(letterId);

        return responseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_LETTER.getMessage());
    }

    // 요청 사용자 id가 받은 편지 리스트 조회
    @GetMapping("/to/{id}")
    public ResponseEntity<?> getReceivedLetters(@RequestParam(value = "size", defaultValue = "5") int size,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @PathVariable("id") Long familyId) {

        return responseUtil.createResponse(HttpMethodCode.GET, letterService.getLetterListByToUser(size, page, familyId));
    }

    // 요청 사용자 id가 보낸 편지 리스트 조회
    @GetMapping("/from/{id}")
    public ResponseEntity<?> getSentLetters(@RequestParam(value = "size", defaultValue = "5") int size,
                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                            @PathVariable("id") Long familyId) {

        return responseUtil.createResponse(HttpMethodCode.GET, letterService.getLetterListByFromUser(size, page, familyId));
    }

    // 편지 내용 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLetterById(@PathVariable("id") Long letterId) {

        return responseUtil.createResponse(HttpMethodCode.GET, letterService.getLetterById(letterId));
    }

    @GetMapping("/receiveCount/{id}")
    public ResponseEntity<?> getLetterCount(@PathVariable("id") Long familyId) {

        return responseUtil.createResponse(HttpMethodCode.GET, letterService.getLetterCount(familyId));
    }

    @GetMapping("/readCount/{id}")
    public ResponseEntity<?> getReadLetterCount(@PathVariable("id") Long familyId) {

        return responseUtil.createResponse(HttpMethodCode.GET, letterService.getReadLetterCount(familyId));
    }

}
