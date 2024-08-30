package com.modernfamily.ukids.domain.game.quizResult.dto;

import com.modernfamily.ukids.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuizResultPaginationResponseDto {

    int size;

    int currentPage;

    int totalPage;

    long totalItems;

    List<QuizResultResponseDto> quizResults;

    UserDto user;

    @Builder
    private QuizResultPaginationResponseDto(int size, int currentPage, int totalPage, long totalItems,
                                            List<QuizResultResponseDto> quizResults, UserDto user) {

        this.size = size;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.totalItems = totalItems;
        this.quizResults = quizResults;
        this.user = user;
    }

    public static QuizResultPaginationResponseDto createResponseDto(int size, int currentPage, int totalPage, long totalItems,
                                                                    List<QuizResultResponseDto> quizResults, UserDto user) {
        return QuizResultPaginationResponseDto.builder()
                .size(size)
                .currentPage(currentPage)
                .totalPage(totalPage)
                .totalItems(totalItems)
                .quizResults(quizResults)
                .user(user)
                .build();
    }
}
