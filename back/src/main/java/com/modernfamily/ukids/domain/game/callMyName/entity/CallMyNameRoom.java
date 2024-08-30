package com.modernfamily.ukids.domain.game.callMyName.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class CallMyNameRoom {
    private String sessionId;
    private boolean isStart;
    private long round;
    private int currentTurn;
    private CallMyNameKeywordType keywordType;
    private HashMap<String, Participate> participantList;
    private List<String> elimiatedParticipants;

    @Builder
    private CallMyNameRoom(String sessionId, boolean isStart, long round, int currentTurn, CallMyNameKeywordType keywordType,
                           HashMap<String, Participate> participantList, List<String> elimiatedParticipants) {
        this.sessionId = sessionId;
        this.isStart = isStart;
        this.round = round;
        this.currentTurn = currentTurn;
        this.keywordType = keywordType;
        this.participantList = participantList;
        this.elimiatedParticipants = elimiatedParticipants;
    }

    public static CallMyNameRoom createCallMyNameRoom(String sessionId) {
        return CallMyNameRoom.builder()
                .sessionId(sessionId)
                .isStart(false)
                .round(0)
                .currentTurn(0)
                .keywordType(null)
                .participantList(new HashMap<String, Participate>())
                .elimiatedParticipants(new ArrayList<>())
                .build();
    }

    public void startGame() {
        this.isStart = true;
    }

    public void enterParticipate(String userId, Participate participate) {
        participantList.put(userId, participate);
    }

    public void exitParticipate(String userId) {
        participantList.remove(userId);
        elimiatedParticipants.add(userId);
    }

    public void generateKeywordType(CallMyNameKeywordType keywordType) {
        this.keywordType = keywordType;
    }

    public void updateCurrentTurn() {
        this.currentTurn = (currentTurn + 1) % participantList.size();
    }

    public void endGame() {
        this.currentTurn = -1;
    }

}
