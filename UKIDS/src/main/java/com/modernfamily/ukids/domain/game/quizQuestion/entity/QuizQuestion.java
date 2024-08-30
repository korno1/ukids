package com.modernfamily.ukids.domain.game.quizQuestion.entity;

import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_delete = false")
public class QuizQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_question_id")
    private Long quizQuestionId;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "quiz_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuizType quizType;

    @Column(name = "is_delete", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;


    @Builder
    private QuizQuestion(String question, String answer, QuizType quizType, boolean isDelete, User writer) {
        this.question = question;
        this.answer = answer;
        this.quizType = quizType;
        this.isDelete = isDelete;
        this.writer = writer;
    }

    public static QuizQuestion createQuizQuestion(String question, String answer, QuizType quizType, User writer) {
        return QuizQuestion.builder()
                .question(question)
                .answer(answer)
                .quizType(quizType)
                .isDelete(false)
                .writer(writer)
                .build();
    }

    public void updateQuizQuestion(Long quizQuestionId) {
        this.quizQuestionId = quizQuestionId;
    }

    public void deleteQuizQuestion() {
        this.isDelete = true;
    }
}
