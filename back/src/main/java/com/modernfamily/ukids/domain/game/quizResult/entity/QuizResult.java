package com.modernfamily.ukids.domain.game.quizResult.entity;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultSaveDto;
import com.modernfamily.ukids.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "quiz_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_result_id")
    private Long quizResultId;

    @Column(name = "correct_counts")
    private long correctCounts;

    @Column(name = "total_counts")
    private long totalCounts;

    @Column(name = "ranking", nullable = false)
    private long ranking;

    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @Builder
    private QuizResult(long correctCounts, long totalCounts, long rank,
                       LocalDate date, User participant, Family family) {
        this.correctCounts = correctCounts;
        this.totalCounts = totalCounts;
        this.ranking = rank;
        this.date = date;
        this.participant = participant;
        this.family = family;
    }

    public static QuizResult createGameResult(QuizResultSaveDto gameResultSaveDto, User participant, Family family) {
        return QuizResult.builder()
                .correctCounts(gameResultSaveDto.getCorrectCounts())
                .totalCounts(gameResultSaveDto.getTotalCounts())
                .rank(gameResultSaveDto.getRank())
                .date(gameResultSaveDto.getDate())
                .participant(participant)
                .family(family)
                .build();
    }

}
