package com.modernfamily.ukids.domain.game.quizQuestion.model.service;

import com.modernfamily.ukids.domain.game.quizQuestion.dto.request.QuizQuestionCreateRequestDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.request.QuizQuestionUpdateRequestDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionListPagenationResponseDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionRandomResponseDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.response.QuizQuestionResponseDto;

import java.util.List;

public interface QuizQuestionService {
    void createQuizQuestion(QuizQuestionCreateRequestDto requestDto);
    void updateQuizQuestion(QuizQuestionUpdateRequestDto requestDto);
    void deleteQuizQuestion(Long quizQuestionId);
    QuizQuestionResponseDto getQuizQuestion(Long quizQuestionId);
    QuizQuestionListPagenationResponseDto getQuizQuestionListByUser(int size, int page);
    List<QuizQuestionRandomResponseDto> chooseRandomQuizQuestion(String userId , long count);
    long getCountQuizQuestionByUser(String id);
}
