package com.modernfamily.ukids.domain.game.quizResult.model.service;

import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultPaginationResponseDto;
import com.modernfamily.ukids.domain.game.quizResult.dto.QuizResultSaveDto;

import java.util.List;

public interface QuizResultService {

    void saveGameResult(List<QuizResultSaveDto> gameResultSaveDtoList);

    QuizResultPaginationResponseDto getQuizResults(int size, int page);

}
