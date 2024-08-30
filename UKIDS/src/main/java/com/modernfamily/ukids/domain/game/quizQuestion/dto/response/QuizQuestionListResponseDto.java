package com.modernfamily.ukids.domain.game.quizQuestion.dto.response;

import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizQuestion;
import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizType;
import com.modernfamily.ukids.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizQuestionListResponseDto {

    private Long quizQuestionId;

    private String question;

    private String answer;

    private QuizType quizType;

    @Builder
    private QuizQuestionListResponseDto(Long quizQuestionId, String question, String answer, QuizType quizType) {
        this.quizQuestionId = quizQuestionId;
        this.question = question;
        this.answer = answer;
        this.quizType = quizType;
    }

    public static QuizQuestionListResponseDto createResponseDto(QuizQuestion quizQuestion){
        return QuizQuestionListResponseDto.builder()
                .quizQuestionId(quizQuestion.getQuizQuestionId())
                .question(quizQuestion.getQuestion())
                .answer(quizQuestion.getAnswer())
                .quizType(quizQuestion.getQuizType())
                .build();
    }
}
