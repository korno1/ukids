package com.modernfamily.ukids.domain.game.quizQuestion.dto.request;

import com.modernfamily.ukids.domain.game.quizQuestion.entity.QuizType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizQuestionUpdateRequestDto {

    @NotNull
    private Long quizQuestionId;
    
    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    @NotBlank
    private QuizType quizType;
}
