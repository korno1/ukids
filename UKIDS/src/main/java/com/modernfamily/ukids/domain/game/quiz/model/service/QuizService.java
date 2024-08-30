package com.modernfamily.ukids.domain.game.quiz.model.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.modernfamily.ukids.domain.game.quiz.dto.QuizRoom;
import com.modernfamily.ukids.domain.game.quiz.model.repository.QuizRepository;
import com.modernfamily.ukids.domain.game.quiz.model.repository.QuizRoomRespository;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionRandomResponseDto;
import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultSaveDto;
import com.modernfamily.ukids.domain.game.quizResult.model.service.QuizResultService;
import com.modernfamily.ukids.domain.webrtc.model.service.WebrtcService;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    // 방 정보
    private Map<Long, QuizRoom> quizRooms;
    private final QuizRepository quizRepository;
    private final QuizRoomRespository quizRoomRespository;
    private final WebrtcService webrtcService;
    private final QuizResultService quizResultService;

    @PostConstruct
    private void init() {
        quizRooms = new HashMap<>();
    }

    // 게임방 생성 -> 있으면 참여, 없으면 생성 + 중복 참여인지 검사 + 유저 참여
    // + webrtc 세션 생성 + connection 반환
    public synchronized Map<String, Object> enterQuizRoom(Long familyId, String userId) throws OpenViduJavaClientException, OpenViduHttpException {
        if(!quizRooms.containsKey(familyId)){
            String sessionId = webrtcService.initializeSessions(null);
            quizRooms.put(familyId, quizRoomRespository.createGameRoom(sessionId));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("type", "ENTER_GAME");
        response.put("id", familyId);
        response.put("webrtcConnection", webrtcService.getToken(quizRooms.get(familyId).getSessionId(), null));

        // 이미 게임 진행 중
        if(quizRoomRespository.isPlaying(quizRooms.get(familyId))){
            response.put("gameRoomInfo", quizRooms.get(familyId));
            return response;
        }

        quizRoomRespository.changeHost(quizRooms.get(familyId), userId);

        // 참여자 목록에 있어
        if(quizRoomRespository.isExistUser(userId, quizRooms.get(familyId))){
            quizRoomRespository.exitGame(userId, quizRooms.get(familyId));
        }

        quizRoomRespository.enterGame(userId, familyId, quizRooms.get(familyId));

        response.put("gameRoomInfo", quizRooms.get(familyId));

        return response;
    }


    // 유저 퇴장
    public Map<String, Object> exitQuizRoom(Long familyId, String userId) {

        isExistFamilyGame(familyId);
        Map<String, Object> response = new HashMap<>();
        response.put("type", "EXIT_GAME");


        if(!quizRoomRespository.isExistUser(userId, quizRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.NOT_FOUND_QUIZ_USER_EXCEPTION);

        if(quizRooms.get(familyId).getHostId().equals(userId))
            quizRoomRespository.nextHost(quizRooms.get(familyId), userId);

        quizRoomRespository.exitGame(userId, quizRooms.get(familyId));

        response.put("gameRoomInfo", quizRooms.get(familyId));
        return response;
    }

    // 게임방 정보 반환
    public Map<String, Object> getQuizRoom(Long familyId) {
        isExistFamilyGame(familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("type", "GET_QUIZ_ROOM");
        response.put("gameResult", quizRooms.get(familyId));

        return response;
    }

    public Map<String, Object> updateMaxQuestionCounts(Long familyId) {
        isExistFamilyGame(familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("type", "GET_MAX_QUESTION_COUNTS");
        quizRoomRespository.updateMaxQuestionCounts(quizRooms.get(familyId));
        response.put("maxCounts", quizRooms.get(familyId).getMaxQuestionCounts());

        return response;
    }

    public void deleteQuizRoom(Long familyId) {
        quizRooms.remove(familyId);
    }

    // 퀴즈 개수 설정
    public Map<String, Object> saveQuizCounts(Long familyId, long counts) {
        isExistFamilyGame(familyId);

        quizRoomRespository.saveQuizCount(counts, quizRooms.get(familyId));

        Map<String, Object> response = new HashMap<>();
        response.put("type", "SET_QUIZ_COUNTS");

        response.put("quizCount", quizRooms.get(familyId).getQuizCount());

        return response;
    }

    // 퀴즈 생성 가능 개수 반환
    public Map<String, Object> getQuizCounts(Long familyId) {
        isExistFamilyGame(familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("type", "QUIZ_COUNTS");
        response.put("quizCount", quizRooms.get(familyId).getQuizCount());

        return response;
    }

    // 준비 클릭 -> 다 준비되면 바로 게임 시작 -> 퀴즈 개수만큼 퀴즈 생성
    public synchronized Map<String, Object> isReadyGameStart(Long familyId, String userId, boolean isReady) {
        isExistFamilyGame(familyId);

        quizRoomRespository.clickReady(userId, quizRooms.get(familyId), isReady);

        Map<String, Object> response = new HashMap<>();
        boolean isState = false;

        // 참가자 모두 준비 완료
        if(quizRoomRespository.checkReady(quizRooms.get(familyId))) {
            quizRoomRespository.changeHost(quizRooms.get(familyId), userId);
            isState = true;

            quizRoomRespository.startGame(quizRooms.get(familyId));
            quizRoomRespository.generateQuiz(quizRooms.get(familyId));

        }
        response.put("type", "IS_READY_GAME");

        response.put("gameStart", isState);
        return response;
    }

    // 질문 반환 -> 반환 끝나면 게임 종료
    public Map<String, Object> getQuizQuestion(Long familyId, String userId) {
        isExistFamilyGame(familyId);

        if(!quizRooms.get(familyId).getHostId().equals(userId))
            return null;

        if(!quizRoomRespository.checkReady(quizRooms.get(familyId)))
            return null;

        QuizQuestionRandomResponseDto quizQuestion = quizRepository.getQuizQuestion(quizRooms.get(familyId));

        Map<String, Object> response = new HashMap<>();
        response.put("type", "QUIZ_QUESTION");

        if(quizQuestion == null) {
            response.put("gameState", "END");

            List<QuizResultSaveDto> result = quizRepository.endGame(familyId, quizRooms.get(familyId));
            if(result != null){
                quizResultService.saveGameResult(quizRepository.endGame(familyId, quizRooms.get(familyId)));
                quizRoomRespository.endGame(quizRooms.get(familyId));
                quizRooms.remove(familyId);
            }
            return response;
        }

        response.put("gameState", "ING");
        response.put("problemIndex", quizRooms.get(familyId).getCurrentQuestionIndex()+1);
        response.put("quizQuestion", quizQuestion);

        return response;
    }

    // 정답 확인
    public Map<String, Object> checkQuizAnswer(Long familyId, String inputAnswer, String userId) {
        isExistFamilyGame(familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("type", "QUIZ_ANSWER");
        response.put("answer", quizRepository.checkAnswer(quizRooms.get(familyId), userId, inputAnswer));

        return response;
    }

    // 게임 종료 -> DB 저장
    public void endGame(Long familyId) {
        quizRepository.endGame(familyId, quizRooms.get(familyId));
        quizRooms.remove(familyId);
    }

    private void isExistFamilyGame(Long familyId) {
        if(!quizRooms.containsKey(familyId))
            throw new NotFoundException("NOT FOUND FAMILY GAME");
    }
}
