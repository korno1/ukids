package com.modernfamily.ukids.domain.game.callMyNameResult.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CallMyNameResultSaveDto {

    private long correctRound;

    private String keyword;

    private long rank;

    private LocalDate date;

    private String participantId;

    private Long familyId;

    @Builder
    private CallMyNameResultSaveDto(long correctRound, String keyword, long rank,
                                    LocalDate date, String participantId, Long familyId) {
        this.correctRound = correctRound;
        this.keyword = keyword;
        this.rank = rank;
        this.date = date;
        this.participantId = participantId;
        this.familyId = familyId;
    }

    public static CallMyNameResultSaveDto createResultDto(long correctRound, String keyword,
                                                          long rank, String participantId, Long familyId) {
        return CallMyNameResultSaveDto.builder()
                .correctRound(correctRound)
                .keyword(keyword)
                .rank(rank)
                .date(LocalDate.now())
                .participantId(participantId)
                .familyId(familyId)
                .build();
    }
}
