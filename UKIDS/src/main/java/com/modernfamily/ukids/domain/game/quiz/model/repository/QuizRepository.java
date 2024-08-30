package com.modernfamily.ukids.domain.game.quiz.model.repository;

import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultSaveDto;
import com.modernfamily.ukids.domain.game.quiz.dto.Participate;
import com.modernfamily.ukids.domain.game.quiz.dto.QuizRoom;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionRandomResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class QuizRepository {

    // 퀴즈 가져오기 및 출제
    public QuizQuestionRandomResponseDto getQuizQuestion(QuizRoom quizRoom){
        if(!quizRoom.increaseCurrentQuestionIndex())
            return null;  // 게임 종료
        log.info("----------current Index------------ : {}", quizRoom.getCurrentQuestionIndex());
        return quizRoom.getRandomQuizQuestionList().get(quizRoom.getCurrentQuestionIndex());
    }

    // 퀴즈 정답 확인
    public String checkAnswer(QuizRoom quizRoom, String userId, String inputAnswer){
        String answer = quizRoom.getRandomQuizQuestionList().get(quizRoom.getCurrentQuestionIndex()).getAnswer();

        if(inputAnswer.equals(answer)) {
            quizRoom.getParticipantList().get(userId).hittingAnswer();
            log.info("answer hit : {}, {}, {}, {}", userId,
                    quizRoom.getParticipantList().get(userId),
                    inputAnswer, answer);
        }
        return answer;
    }


    // 게임 종료 및 퀴즈 결과 저장
    public List<QuizResultSaveDto> endGame(Long familyId, QuizRoom quizRoom){

        quizRoom.saveGame();
        if(quizRoom.getSaveCounts() < quizRoom.getParticipantList().size())
            return null;

        List<Map.Entry<String, Participate>> entryList = new ArrayList<>(quizRoom.getParticipantList().entrySet());
        entryList.sort(Comparator.comparing((Map.Entry<String, Participate> entry) -> entry.getValue().getHit()).reversed());

        long totalCounts = quizRoom.getParticipantList().size() * quizRoom.getQuizCount();
        long prevCounts = 0;
        long rank = 1L;
        List<QuizResultSaveDto> gameResultSaveDtoList = new ArrayList<>();
        for(Map.Entry<String, Participate> entry : entryList){
            if(prevCounts == entry.getValue().getHit())
                rank--;
            gameResultSaveDtoList.add(QuizResultSaveDto.createGameResultDto(entry.getValue().getHit(),
                    totalCounts, rank, entry.getKey(), familyId));
            prevCounts = entry.getValue().getHit();
            rank++;
        }
        quizRoom.saveGame();
        return gameResultSaveDtoList;
    }

}
