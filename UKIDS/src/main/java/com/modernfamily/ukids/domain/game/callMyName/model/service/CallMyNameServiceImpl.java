package com.modernfamily.ukids.domain.game.callMyName.model.service;

import com.modernfamily.ukids.domain.game.callMyName.entity.CallMyNameRoom;
import com.modernfamily.ukids.domain.game.callMyName.entity.Participate;
import com.modernfamily.ukids.domain.game.callMyName.entity.CallMyNameKeywordType;
import com.modernfamily.ukids.domain.game.callMyName.model.repository.CallMyNameRepository;
import com.modernfamily.ukids.domain.game.callMyName.model.repository.CallMyNameRoomRepository;
import com.modernfamily.ukids.domain.webrtc.model.service.WebrtcService;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallMyNameServiceImpl implements CallMyNameService {

    // 방 정보
    private Map<Long, CallMyNameRoom> callMyNameRooms;
    private final CallMyNameRepository callMyNameRepository;
    private final CallMyNameRoomRepository callMyNameRoomRepository;
    private final CallMyNameKeywordService keywordService;
    private final WebrtcService webrtcService;

    @PostConstruct
    private void init() {
        callMyNameRooms = new HashMap<>();
    }

    // 게임방 생성
    @Override
    public Map<String, Object> enterCallMyNameRoom(Long familyId, String userId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        if (!callMyNameRooms.containsKey(familyId)) {
            String sessionId = webrtcService.initializeSessions(null);
            callMyNameRoomRepository.createCallMyNameRoom(sessionId);
        }

        if (callMyNameRoomRepository.isExistUser(userId, callMyNameRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.DUPLICATED_ID_EXCEPTION);

        if (callMyNameRoomRepository.isPlaying(callMyNameRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.ALREADY_PLAYING_EXCEPTION);

        Map<String, Object> response = new HashMap<>();
        response.put("id", familyId);
        response.put("webrtcConnection", webrtcService.getToken(callMyNameRooms.get(familyId).getSessionId(), null));

        // 최초 입장하는 참가자일 시 host 권한 부여
        boolean isHost = false;
        if (callMyNameRooms.get(familyId).getParticipantList().isEmpty()) isHost = true;

        callMyNameRoomRepository.enterGame(userId, isHost, callMyNameRooms.get(familyId));

        response.put("callMyNameRoomInfo", callMyNameRooms.get(familyId));

        return response;
    }

    // 참가자 퇴장
    @Override
    public void exitCallMyNameRoom(Long familyId, String userId) {

        isExistFamilyGame(familyId);

        if (!callMyNameRoomRepository.isExistUser(userId, callMyNameRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.NOT_FOUND_CALL_MY_NAME_EXCEPTION);

        callMyNameRoomRepository.exitGame(userId, callMyNameRooms.get(familyId));
    }

    // 게임방 정보 반환
    @Override
    public CallMyNameRoom getCallMyNameRoom(Long familyId) {

        return callMyNameRooms.get(familyId);
    }

    @Override
    public Map<String, Object> isReadyGameStart(Long familyId, String userId) {

        isExistFamilyGame(familyId);

        callMyNameRoomRepository.clickReady(userId, callMyNameRooms.get(familyId));

        Map<String, Object> response = new HashMap<>();
        boolean isState = true;

        // 참가자 모두 준비 안됨
        if (!callMyNameRoomRepository.checkReady(callMyNameRooms.get(familyId)))
            isState = false;

        callMyNameRoomRepository.startGame(callMyNameRooms.get(familyId));

        response.put("gameStart", isState);
        return response;
    }

    // 키워드 타입(카테고리) 설정
    // host만 가능
    @Override
    public void getKeywordType(Long familyId, String type, String userId) {
        if (!callMyNameRooms.get(familyId).getParticipantList().get(userId).isHost())
            throw new ExceptionResponse(CustomException.NOT_HOST_USER_EXCEPTION);

        // String 타입인 keywordType을 통해 CallMyNameKeywordType 검색
        CallMyNameKeywordType keywordType = CallMyNameKeywordType.findByTitle(type);

        callMyNameRooms.get(familyId).generateKeywordType(keywordType);
    }

    // 게임 시작 후 키워드 정하기
    @Override
    public void assignKeyword(Long familyId) {
        generateKeyword(familyId);

        List<String> keywords = generateKeyword(familyId);
        Map<String, Participate> participateList = callMyNameRooms.get(familyId).getParticipantList();

        int index = 0;
        for (Participate participate : participateList.values()) {
            if (index < keywords.size()) {
                participate.generateKeyword(keywords.get(index));
                index++;
            } else throw new ExceptionResponse(CustomException.NOT_ENOUGH_KEYWORDS_EXCEPTION);
        }
    }

    @Override
    public Map<String, Object> getCurrentTurn(Long familyId) {
        long current = callMyNameRooms.get(familyId).getCurrentTurn();

        Map<String, Object> response = new HashMap<>();
        response.put("currentTurn", current);

        return response;
    }

    // 질문
    @Override
    public void question(Long familyId, String userId) {
        isExistFamilyGame(familyId);

        callMyNameRooms.get(familyId).getParticipantList().get(userId).nextRound();
        // 턴 넘어가기
        callMyNameRooms.get(familyId).updateCurrentTurn();
    }

    // 정답 확인
    @Override
    public Map<String, Object> checkAnswer(Long familyId, String inputAnswer, String userId) {

        isExistFamilyGame(familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("answer", callMyNameRepository.checkAnswer(callMyNameRooms.get(familyId), userId, inputAnswer));

        // 만약 남은 참가자가 1명이라면 게임 종료
        if (callMyNameRooms.get(familyId).getParticipantList().size() == 1) {
            endGame(familyId);
        }

        // boolean 타입을 반환
        // true: 정답
        // false: 오답
        return response;
    }

    // 게임 종료
    @Override
    public void endGame(Long familyId) {
        callMyNameRepository.endGame(familyId, callMyNameRooms.get(familyId));
        callMyNameRooms.remove(familyId);
        callMyNameRooms.get(familyId).endGame();
    }

    private void isExistFamilyGame(Long familyId) {
        if (!callMyNameRooms.containsKey(familyId))
            throw new ExceptionResponse(CustomException.NOT_FOUND_CALL_MY_NAME_EXCEPTION);
    }

    private List<String> generateKeyword(Long familyId) {

        int participateSize = callMyNameRooms.get(familyId).getParticipantList().size();
        String keywordType = callMyNameRooms.get(familyId).getKeywordType().getTitle();

        return keywordService.generateCallMyNameKeyword(keywordType, participateSize);
    }

}
