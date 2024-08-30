package com.modernfamily.ukids.domain.game.quizResult.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class QuizResultSaveDto {

    private long correctCounts;

    private long totalCounts;

    private long rank;

    private LocalDate date;

    private String participantId;

    private Long familyId;

    @Builder
    private QuizResultSaveDto(long correctCounts, long totalCounts, long rank,
                              LocalDate date, String participantId, Long familyId) {
        this.correctCounts = correctCounts;
        this.totalCounts = totalCounts;
        this.rank = rank;
        this.date = date;
        this.participantId = participantId;
        this.familyId = familyId;
    }

    public static QuizResultSaveDto createGameResultDto(long correctCounts, long totalCounts,
                                                        long rank, String participantId, Long familyId) {
        return QuizResultSaveDto.builder()
                .correctCounts(correctCounts)
                .totalCounts(totalCounts)
                .rank(rank)
                .date(LocalDate.now())
                .participantId(participantId)
                .familyId(familyId)
                .build();
    }
}
