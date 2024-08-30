package com.modernfamily.ukids.domain.game.quizQuestion.dto.response;

import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizQuestion;
import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizType;
import com.modernfamily.ukids.domain.user.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizQuestionResponseDto {

    private Long quizQuestionId;

    private String question;

    private String answer;

    private QuizType quizType;

    private UserDto writer;

    @Builder
    private QuizQuestionResponseDto(Long quizQuestionId, String question, String answer, QuizType quizType, UserDto writer) {
        this.quizQuestionId = quizQuestionId;
        this.question = question;
        this.answer = answer;
        this.quizType = quizType;
        this.writer = writer;
    }

    public static QuizQuestionResponseDto createResponseDto(QuizQuestion quizQuestion, UserDto writer){
        return QuizQuestionResponseDto.builder()
                .quizQuestionId(quizQuestion.getQuizQuestionId())
                .question(quizQuestion.getQuestion())
                .answer(quizQuestion.getAnswer())
                .quizType(quizQuestion.getQuizType())
                .writer(writer)
                .build();
    }
}
