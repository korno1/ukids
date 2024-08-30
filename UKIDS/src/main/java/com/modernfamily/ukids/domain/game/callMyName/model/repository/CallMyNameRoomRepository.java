package com.modernfamily.ukids.domain.game.callMyName.model.repository;

import com.modernfamily.ukids.domain.game.callMyName.entity.CallMyNameRoom;
import com.modernfamily.ukids.domain.game.callMyName.entity.Participate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CallMyNameRoomRepository {

    // 게임방 생성
    public CallMyNameRoom createCallMyNameRoom(String sessionId) {
        return CallMyNameRoom.createCallMyNameRoom(sessionId);
    }

    // 게임 참여
    public void enterGame(String userId, boolean isHost, CallMyNameRoom callMyNameRoom) {
        callMyNameRoom.enterParticipate(userId, Participate.createParticipate(isHost));
    }

    // 게임 중복 참여 여부 판별
    public boolean isExistUser(String userId, CallMyNameRoom callMyNameRoom) {
        if (callMyNameRoom.getParticipantList().containsKey(userId))
            return true;

        return false;
    }

    // 참가자 퇴장
    public void exitGame(String userId, CallMyNameRoom callMyNameRoom) {
        callMyNameRoom.exitParticipate(userId);
    }

    // 참가자 준비 상태 확인
    public boolean checkReady(CallMyNameRoom callMyNameRoom) {
        for (Participate participate : callMyNameRoom.getParticipantList().values()) {
            if (!participate.isReady()) return false;
        }
        return true;
    }

    // 참가자 준비 버튼 클릭
    public void clickReady(String userId, CallMyNameRoom callMyNameRoom) {
        callMyNameRoom.getParticipantList().get(userId).clickReady();
    }

    // 게임 시작
    public void startGame(CallMyNameRoom callMyNameRoom) {
        callMyNameRoom.startGame();
    }

    // 게임 진행중인지 확인
    public boolean isPlaying(CallMyNameRoom callMyNameRoom) {
        return callMyNameRoom.isStart();
    }

}
