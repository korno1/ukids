package com.modernfamily.ukids.domain.game.quiz.model.service;

import com.modernfamily.ukids.domain.game.gameResult.entity.GameType;
import com.modernfamily.ukids.domain.game.quiz.dto.QuizRoom;
import com.modernfamily.ukids.domain.game.quiz.model.repository.QuizRepository;
import com.modernfamily.ukids.domain.game.quiz.model.repository.QuizRoomRespository;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionRandomResponseDto;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.webrtc.model.service.WebrtcService;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    // 방 정보
    private Map<Long, QuizRoom> quizRooms;
    private final QuizRepository quizRepository;
    private final QuizRoomRespository quizRoomRespository;
    private final FamilyMemberValidator familyMemberValidator;
    private final WebrtcService webrtcService;

    @PostConstruct
    private void init() {
        quizRooms = new HashMap<>();
    }

    // 게임방 생성 -> 있으면 참여, 없으면 생성 + 중복 참여인지 검사 + 유저 참여
    // + webrtc 세션 생성 + connection 반환
    public Map<String, Object> enterQuizRoom(Long familyId, GameType gameType) throws OpenViduJavaClientException, OpenViduHttpException {
        User participate = familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        if(!quizRooms.containsKey(familyId)){
            String sessionId = webrtcService.initializeSessions(null);
            quizRoomRespository.createGameRoom(gameType, sessionId);
        }

        // 참여자 목록에 있어
        if(quizRoomRespository.isExistUser(participate.getUserId(), quizRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.DUPLICATED_ID_EXCEPTION);

        // 이미 게임 진행 중
        if(quizRoomRespository.isPlaying(quizRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.ALREADY_PLAYING_EXCEPTION);

        Map<String, Object> response = new HashMap<>();
        response.put("id", familyId);
        response.put("webrtcConnection", webrtcService.createConnection(quizRooms.get(familyId).getSessionId(), null));

        quizRoomRespository.enterGame(participate.getUserId(), quizRooms.get(familyId));

        response.put("gameRoomInfo", quizRooms.get(familyId));

        return response;
    }


    // 유저 퇴장
    public void exitQuizRoom(Long familyId) {
        User participate = familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        if(!quizRooms.containsKey(familyId))
            throw new ExceptionResponse(CustomException.NOT_FOUND_QUIZ_GAME_EXCEPTION);

        if(!quizRoomRespository.isExistUser(participate.getUserId(), quizRooms.get(familyId)))
            throw new ExceptionResponse(CustomException.NOT_FOUND_QUIZ_USER_EXCEPTION);

        quizRoomRespository.exitGame(participate.getUserId(), quizRooms.get(familyId));
    }

    // 게임방 정보 반환
    public QuizRoom getQuizRoom(Long familyId) {
        familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        return quizRooms.get(familyId);
    }

    // 퀴즈 개수 설정
    public QuizRoom saveQuizCounts(Long familyId, long counts) {
        familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        quizRoomRespository.saveQuizCount(counts, quizRooms.get(familyId));

        return quizRooms.get(familyId);
    }
    
    // 퀴즈 생성 가능 개수 반환
    public long getQuizCounts(Long familyId) {
        familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        return quizRooms.get(familyId).getQuizCount();
    }

    // 준비 클릭 -> 다 준비되면 바로 게임 시작 -> 퀴즈 개수만큼 퀴즈 생성
    public boolean isReadyGameStart(Long familyId) {
        User participate = familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        quizRoomRespository.clickReady(participate.getUserId(), quizRooms.get(familyId));

        // 참가자 모두 준비 안됨
        if(!quizRoomRespository.checkReady(quizRooms.get(familyId)))
            return false;

        quizRoomRespository.generateQuiz(quizRooms.get(familyId));
        quizRoomRespository.startGame(quizRooms.get(familyId));
        return true;
    }

    // 질문 반환 -> 반환 끝나면 게임 종료
    public QuizQuestionRandomResponseDto getQuizQuestion(Long familyId) {
        familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        return quizRepository.getQuizQuestion(quizRooms.get(familyId));
    }

    // 정답 확인
    public String checkQuizAnswer(Long familyId, String inputAnswer) {
        User participate = familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        return quizRepository.checkAnswer(quizRooms.get(familyId), participate.getUserId(), inputAnswer);
    }

    // 게임 종료 -> DB 저장
    public void endGame(Long familyId) {
        familyMemberValidator.checkUserInFamilyMember(familyId).getUser();

        quizRepository.endGame(familyId, quizRooms.get(familyId));
        quizRooms.remove(familyId);
    }
}
