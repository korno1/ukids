package com.modernfamily.ukids.domain.game.callMyName.model.repository;

import com.modernfamily.ukids.domain.game.callMyNameResult.dto.CallMyNameResultSaveDto;
import com.modernfamily.ukids.domain.game.callMyName.entity.CallMyNameRoom;
import com.modernfamily.ukids.domain.game.callMyName.entity.Participate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Repository
public class CallMyNameRepository {

    // 키워드 정답 확인
    public boolean checkAnswer(CallMyNameRoom callMyNameRoom, String userId, String inputAnswer) {
        Map<String, Participate> participateMap = callMyNameRoom.getParticipantList();
        String answer = participateMap.get(userId).getKeyword();

        if (inputAnswer.equals(answer)) {
            participateMap.get(userId).correct();
            participateMap.remove(userId);
        } else {
            // 다음 라운드 진출
            participateMap.get(userId).nextRound();
            // 다음 턴 참가자 idx 업데이트
            callMyNameRoom.updateCurrentTurn();
        }

        return inputAnswer.equals(answer);
    }

    // 게임 종료 및 게임 결과 저장
    public List<CallMyNameResultSaveDto> endGame(Long familyId, CallMyNameRoom callMyNameRoom) {

        List<Map.Entry<String, Participate>> entryList = new ArrayList<>(callMyNameRoom.getParticipantList().entrySet());

        // 참가자 리스트를 정렬 (정답을 맞춘 순서에 따라, 그 다음 라운드 순서에 따라)
        entryList.sort((entry1, entry2) -> {
            Participate p1 = entry1.getValue();
            Participate p2 = entry2.getValue();

            // isCorrect가 true인 참가자는 작은 round 우선
            if (p1.isCorrect() && !p2.isCorrect()) {
                return -1;
            } else if (!p1.isCorrect() && p2.isCorrect()) {
                return 1;
            }
            // 두 참가자가 모두 맞췄거나 모두 틀린 경우
            return Long.compare(p1.getRound(), p2.getRound());
        });

        long rank = 1L;
        long prevRound = -1;
        List<CallMyNameResultSaveDto> gameResultSaveDtoList = new ArrayList<>();

        for (Map.Entry<String, Participate> entry : entryList) {
            Participate participate = entry.getValue();
            long roundToUse = participate.isCorrect() ? participate.getRound() : Integer.MAX_VALUE;

            if (roundToUse != prevRound) {
                rank = gameResultSaveDtoList.size() + 1;
            }

            gameResultSaveDtoList.add(CallMyNameResultSaveDto.createResultDto(
                    participate.getRound(),
                    participate.getKeyword(),
                    roundToUse == Integer.MAX_VALUE ? -1 : rank,
                    entry.getKey(),
                    familyId
            ));
            prevRound = roundToUse;
        }

        gameResultSaveDtoList.sort(Comparator.comparing(CallMyNameResultSaveDto::getRank));

        return gameResultSaveDtoList;
    }
}
