package com.modernfamily.ukids.domain.game.callMyName.model.service;

import com.modernfamily.ukids.domain.game.callMyName.entity.CallMyNameRoom;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

import java.util.Map;

public interface CallMyNameService {

    Map<String, Object> enterCallMyNameRoom(Long familyId, String userId)
            throws OpenViduJavaClientException, OpenViduHttpException;

    void exitCallMyNameRoom(Long familyId, String userId);

    CallMyNameRoom getCallMyNameRoom(Long familyId);

    Map<String, Object> isReadyGameStart(Long familyId, String userId);

    void getKeywordType(Long familyId, String type, String userId);

    void assignKeyword(Long familyId);

    Map<String, Object> getCurrentTurn(Long familyId);

    void question(Long familyId, String userId);

    Map<String, Object> checkAnswer(Long familyId, String inputAnswer, String userId);

    void endGame(Long familyId);

}
