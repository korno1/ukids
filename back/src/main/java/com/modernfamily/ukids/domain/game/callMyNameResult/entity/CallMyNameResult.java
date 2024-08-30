package com.modernfamily.ukids.domain.game.callMyNameResult.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.game.callMyNameResult.dto.CallMyNameResultSaveDto;
import com.modernfamily.ukids.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CallMyNameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_result_id")
    private Long gameResultId;

    @Column(name = "correct_round")
    private Long correctRound;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "rank", nullable = false)
    private Long rank;

    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @Builder
    private CallMyNameResult(Long correctRound, String keyword, Long rank,
                             LocalDate date, User participant, Family family) {
        this.correctRound = correctRound;
        this.keyword = keyword;
        this.rank = rank;
        this.date = date;
        this.participant = participant;
        this.family = family;
    }

    public static CallMyNameResult createResult(CallMyNameResultSaveDto callMyNameResultSaveDto, User participant, Family family) {
        return CallMyNameResult.builder()
                .correctRound(callMyNameResultSaveDto.getCorrectRound())
                .keyword(callMyNameResultSaveDto.getKeyword())
                .rank(callMyNameResultSaveDto.getRank())
                .date(callMyNameResultSaveDto.getDate())
                .participant(participant)
                .family(family)
                .build();
    }

}