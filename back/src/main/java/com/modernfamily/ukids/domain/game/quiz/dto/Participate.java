package com.modernfamily.ukids.domain.game.quiz.dto;

import com.modernfamily.ukids.domain.familyMember.entity.FamilyRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Participate {
    private String userName;
    private FamilyRole role;
    private long maxQuestion;
    private long hit;
    private boolean isReady;

    private Participate(String userName, FamilyRole role, long maxQuestion, long hit, boolean isReady) {
        this.userName = userName;
        this.role = role;
        this.maxQuestion = maxQuestion;
        this.hit = hit;
        this.isReady = isReady;
    }

    public static Participate createParticipate(String userName, FamilyRole role, long maxQuestion) {
        return new Participate(userName, role, maxQuestion, 0, false);
    }

    public void hittingAnswer(){
        this.hit++;
    }

    public void clickReady(boolean isReady){
        this.isReady = isReady;
    }
}
