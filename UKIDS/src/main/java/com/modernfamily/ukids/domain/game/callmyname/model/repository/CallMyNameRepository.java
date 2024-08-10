package com.modernfamily.ukids.domain.game.callmyname.model.repository;

import com.modernfamily.ukids.domain.game.callmyname.dto.CallMyNameRoom;
import com.modernfamily.ukids.domain.game.callmyname.dto.Participate;
import com.modernfamily.ukids.domain.game.gameResult.dto.GameResultSaveDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Repository
public class CallMyNameRepository {

    // 키워드 가져오기 및 출제

    // 키워드 정답 확인
    public String checkAnswer(CallMyNameRoom callMyNameRoom, String userId, String inputAnswer) {
//        String answer = callMyNameRoom.getRandomKeywordList().get();
        String answer = "";

        if (inputAnswer.equals(answer))
            callMyNameRoom.getParticipantList().get(userId).correct();

        return answer;
    }

    // 게임 종료 및 게임 결과 저장
    // 미완
    public List<GameResultSaveDto> endGame(Long familyId, CallMyNameRoom callMyNameRoom) {

        List<Map.Entry<String, Participate>> entryList = new ArrayList<>(callMyNameRoom.getParticipantList().entrySet());
        entryList.sort(Comparator.comparing((Map.Entry<String, Participate> entry) -> entry.getValue().getTurn()));

        Long rank = 1L;
        List<GameResultSaveDto> gameResultSaveDtoList = new ArrayList<>();
        for (Map.Entry<String, Participate> entry : entryList) {
            // GameResult 저정해야함
        }

        return gameResultSaveDtoList;
    }

}
