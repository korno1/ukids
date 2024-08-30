package com.modernfamily.ukids.domain.game.callMyName.controller;

import com.modernfamily.ukids.domain.game.callMyName.entity.CallMyNameRoom;
import com.modernfamily.ukids.domain.game.callMyName.model.service.CallMyNameService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CallMyNameController {

    private final CallMyNameService callMyNameService;

    // 게임방 생성
    @MessageMapping("/call/enter/{id}")
    @SendTo("/topic/call/{id}")
    public Map<String, Object> enterCallMyNameRoom(@PathVariable("id") Long familyId,
                                                   SimpMessageHeaderAccessor headerAccessor)
            throws OpenViduJavaClientException, OpenViduHttpException {
        String userId = headerAccessor.getUser().getName();
        return callMyNameService.enterCallMyNameRoom(familyId, userId);
    }

    // 참가자 퇴장
    @MessageMapping("/call/exit/{id}")
    public void exitCallMyNameRoom(@PathVariable("id") Long familyId, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        callMyNameService.exitCallMyNameRoom(familyId, userId);
    }

    // 게임방 정보 반환
    @MessageMapping("/call/info/{id}")
    @SendTo("/topic/call/{id}")
    public CallMyNameRoom getCallMyNameRoom(@PathVariable("id") Long familyId) {
        return callMyNameService.getCallMyNameRoom(familyId);
    }

    // 준비 클릭
    @MessageMapping("/call/ready/{id}")
    @SendTo("/topic/call/{id}")
    public Map<String, Object> isReadyGameStart(@PathVariable("id") Long familyId, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        return callMyNameService.isReadyGameStart(familyId, userId);
    }

    // 키워드 타입(카테고리) 설정
    // host만 가능
    @MessageMapping("/call/keyword-type/{id}")
    @SendTo("/topic/call/{id}")
    public void saveCategory(@PathVariable("id") Long familyId,
                             @RequestParam("type") String type,
                             SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        // 입력된 category와 CallMyNameKeywordType의 title 과 일치되는 것 찾기
        callMyNameService.getKeywordType(familyId, type, userId);
    }

    // 키워드 타입에 따른 키워드 할당
    @MessageMapping("/call/keyword/{id}")
    public void assignKeyword(@PathVariable("id") Long familyId) {

        callMyNameService.assignKeyword(familyId);
    }

    // CallMyNameRoom의 currentTurn 반환
    @MessageMapping("/call/current-turn/{id}")
    @SendTo("/topic/call/{id}")
    public Map<String, Object> getCurrentTurn(@PathVariable("id") Long familyId) {

        return callMyNameService.getCurrentTurn(familyId);
    }

    // 질문하기
    @MessageMapping("/call/question/{id}")
//    @SendTo("/topic/call/{id}")
    public void question(@PathVariable("id") Long familyId, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        callMyNameService.question(familyId, userId);
    }

    // 정답 확인
    @MessageMapping("/call/answer/{id}")
    @SendTo("/topic/call/{id}")
    public Map<String, Object> checkAnswer(@PathVariable("id") Long familyId,
                                           @RequestParam("inputAnswer") String inputAnswer,
                                           SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        return callMyNameService.checkAnswer(familyId, inputAnswer, userId);
    }

    // 게임 결과 반환


}
