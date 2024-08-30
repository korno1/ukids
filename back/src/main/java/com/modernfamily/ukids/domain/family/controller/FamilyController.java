package com.modernfamily.ukids.domain.family.controller;

import com.modernfamily.ukids.domain.family.dto.*;
import com.modernfamily.ukids.domain.family.message.SuccessMessage;
import com.modernfamily.ukids.domain.family.model.service.FamilyService;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;
    private final HttpResponseUtil responseUtil;

    // 가족방 정보
    @GetMapping("/{familyId}")
    public ResponseEntity<Map<String, Object>> getFamily(@PathVariable("familyId") Long familyId){
        FamilyResponseDto familyResponseDto = familyService.getFamily(familyId);

        return responseUtil.createResponse(HttpMethodCode.GET, familyResponseDto);
    }

    // 로그인 한 유저가 속한(승인된) 가족방 리스트
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getFamilies(){
        List<FamilyListResponseDto> familyListResponseDtos = familyService.getFamilies();

        return responseUtil.createResponse(HttpMethodCode.GET, familyListResponseDtos);
    }

    // 가족방 생성
    @PostMapping
    public ResponseEntity<Map<String,Object>> createFamily(@RequestBody FamilyRequestDto familyRequestDto){
        Long familyId = null;
        try {
            familyId = familyService.createFamily(familyRequestDto);

        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionResponse(CustomException.NOSUCH_ALGORITHM_EXCEPTION);
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("familyId", familyId);
        return responseUtil.createResponse(HttpMethodCode.POST, responseMap);

    }

    // 고유코드로 가족방 찾기
    @GetMapping("/search/{code}")
    public ResponseEntity<Map<String,Object>> searchFamily(@PathVariable("code") String code){
        FamilySearchResponseDto familyDto = familyService.findByCode(code);

        return responseUtil.createResponse(HttpMethodCode.GET, familyDto);
    }

    @PostMapping("/pwcheck")
    public ResponseEntity<Map<String,Object>> checkPassword(@RequestBody FamilyPasswordDto familyPasswordDto){
       boolean check = familyService.checkPassword(familyPasswordDto);

       if(check)
            return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_PASSWORD_SAME);

       return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_PASSWORD_DISCORD);
    }

    @PutMapping
    public ResponseEntity<Map<String,Object>> updateFamily(@RequestBody FamilyUpdateDto familyUpdateDto){
        familyService.updateFamily(familyUpdateDto);

        return responseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_FAMILY);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteFamily(@RequestBody FamilyPasswordDto familyPasswordDto){
        familyService.deleteFamily(familyPasswordDto);

        return responseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_FAMILY);
    }
}
