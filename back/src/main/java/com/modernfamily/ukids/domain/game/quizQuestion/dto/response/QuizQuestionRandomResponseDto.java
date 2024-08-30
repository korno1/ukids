package com.modernfamily.ukids.domain.game.quizQuestion.dto.response;

import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizQuestion;
import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizType;
import com.modernfamily.ukids.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizQuestionRandomResponseDto {

    private Long quizQuestionId;

    private String question;

    private String answer;

    private QuizType quizType;

    private List<String> wrongAnswer;

    private UserDto writer;

    @Builder
    private QuizQuestionRandomResponseDto(Long quizQuestionId, String question, String answer, QuizType quizType, UserDto writer, List<String> wrongAnswer) {
        this.quizQuestionId = quizQuestionId;
        this.question = question;
        this.answer = answer;
        this.quizType = quizType;
        this.writer = writer;
        this.wrongAnswer = wrongAnswer;
    }

    public static QuizQuestionRandomResponseDto createResponseDto(QuizQuestion quizQuestion, UserDto writer, List<String> wrongAnswer){
        return QuizQuestionRandomResponseDto.builder()
                .quizQuestionId(quizQuestion.getQuizQuestionId())
                .question(quizQuestion.getQuestion())
                .answer(quizQuestion.getAnswer())
                .quizType(quizQuestion.getQuizType())
                .writer(writer)
                .wrongAnswer(wrongAnswer)
                .build();
    }
}
