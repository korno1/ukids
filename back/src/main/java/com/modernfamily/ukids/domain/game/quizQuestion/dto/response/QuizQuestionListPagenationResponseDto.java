package com.modernfamily.ukids.domain.game.quizQuestion.dto.response;

import com.modernfamily.ukids.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizQuestionListPagenationResponseDto {

    private int size;

    private int currentPage;

    private int totalPages;

    private long totalItems;

    private List<QuizQuestionListResponseDto> quizQuestions;

    private UserDto writer;

    @Builder
    private QuizQuestionListPagenationResponseDto(int size, int currentPage, int totalPages, long totalItems,
                                                  List<QuizQuestionListResponseDto> quizQuestions, UserDto writer) {
        this.size = size;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.quizQuestions = quizQuestions;
        this.writer = writer;
    }

    public static QuizQuestionListPagenationResponseDto createResponseDto(int size, int currentPage, int totalPages, long totalItems,
                                                                          List<QuizQuestionListResponseDto> quizQuestions, UserDto writer){
        return QuizQuestionListPagenationResponseDto.builder()
                .size(size)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .quizQuestions(quizQuestions)
                .writer(writer)
                .build();
    }
}
