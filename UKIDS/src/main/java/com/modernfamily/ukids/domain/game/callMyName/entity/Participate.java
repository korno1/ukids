package com.modernfamily.ukids.domain.game.callMyName.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Participate {
    private long round;
    private String keyword;
    private boolean isReady;
    private boolean isCorrect;
    private boolean isHost;

    private Participate(long round, String keyword, boolean isReady, boolean isCorrect, boolean isHost) {
        this.round = round;
        this.keyword = keyword;
        this.isReady = isReady;
        this.isCorrect = isCorrect;
        this.isHost = isHost;
    }

    public static Participate createParticipate(boolean isHost) {
        return new Participate(1, null, true, false, isHost);
    }

    public void generateKeyword(String keyword) {
        this.keyword = keyword;
    }

//    public void wrongAnswer() {
//        this.round++;
//    }
    public void nextRound() {
        this.round++;
    }

    public void clickReady() {
        this.isReady = !this.isReady;
    }

    public void correct() {
        this.isCorrect = true;
    }

}
