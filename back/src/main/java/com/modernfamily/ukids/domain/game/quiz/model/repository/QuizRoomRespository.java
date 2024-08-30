package com.modernfamily.ukids.domain.game.quiz.model.repository;

import com.amazonaws.services.kms.model.NotFoundException;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.domain.game.quiz.dto.Participate;
import com.modernfamily.ukids.domain.game.quiz.dto.QuizRoom;
import com.modernfamily.ukids.domain.game.quizQuestion.model.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QuizRoomRespository {

    private final QuizQuestionService quizQuestionService;
    private final FamilyMemberRepository familyMemberRepository;

    // 게임방 만들기
    public QuizRoom createGameRoom(String sessionId){
        return QuizRoom.createQuizRoom(sessionId);
    }

    // 유저 참여
    public void enterGame(String userId, long familyId, QuizRoom quizRoom){
        long maxCounts = quizQuestionService.getCountQuizQuestionByUser(userId);

        Optional<FamilyMember> familyMember = familyMemberRepository.findByUser_IdAndFamily_FamilyId(userId, familyId);

        if(familyMember.isEmpty() || familyMember == null)
            throw new NotFoundException("NOT FOUND FAMILYMEMBER EXCEPTION");
        quizRoom.enterParticipate(userId, Participate.createParticipate(familyMember.get().getUser().getName(),
                familyMember.get().getRole(), maxCounts));
    }

    // 게임 중복 참여 여부 판별
    public boolean isExistUser(String userId, QuizRoom quizRoom){
        if(quizRoom.getParticipantList().containsKey(userId))
            return true;

        return false;
    }

    // 유저 퇴장
    public void exitGame(String userId, QuizRoom quizRoom){
        quizRoom.exitParticipate(userId);
    }

    // 퀴즈 개수 설정
    public void saveQuizCount(long counts, QuizRoom quizRoom){
        quizRoom.updateQuizCount(counts);
    }

    // 최대 퀴즈 개수 업데이트
    public void updateMaxQuestionCounts(QuizRoom quizRoom) {
        quizRoom.updateMaxQuestionCounts();
    }

    // 참가자 준비 상태 확인
    public boolean checkReady(QuizRoom quizRoom){
        for(Participate participate : quizRoom.getParticipantList().values()){
            if(!participate.isReady()) return false;
        }
        return true;
    }

    // 참가자 준비 버튼 클릭
    public void clickReady(String userId, QuizRoom quizRoom, boolean isReady){
        quizRoom.getParticipantList().get(userId).clickReady(isReady);
    }

    // 게임 시작
    public void startGame(QuizRoom quizRoom){
        quizRoom.startGame();
    }

    // 게임 종료
    public void endGame(QuizRoom quizRoom){
        quizRoom.endGame();
    }

    // 게임 중인지 확인
    public boolean isPlaying(QuizRoom quizRoom){
        return quizRoom.isStart();
    }

    public void changeHost(QuizRoom quizRoom, String hostId){
        quizRoom.changeHost(hostId);
    }

    public void nextHost(QuizRoom quizRoom, String hostId){
        quizRoom.nextHost(hostId);
    }

    // 퀴즈 생성
    public synchronized void generateQuiz(QuizRoom quizRoom){
        long quizCounts = quizRoom.getQuizCount();
        quizRoom.initQuizList();
        for(Map.Entry<String, Participate> entrySet : quizRoom.getParticipantList().entrySet()){
            log.info("choose random participate {}", entrySet.getKey());
            quizRoom.generateRandomQuizQuestion(
                    quizQuestionService.chooseRandomQuizQuestion(entrySet.getKey(), quizCounts)
            );
        }
    }
}
