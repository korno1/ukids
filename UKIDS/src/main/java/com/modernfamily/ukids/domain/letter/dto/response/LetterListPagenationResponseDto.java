package com.modernfamily.ukids.domain.letter.dto.response;

import com.modernfamily.ukids.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LetterListPagenationResponseDto {

    private int size;

    private int currentPage;

    private int totalPages;

    private long totalItems;

    private List<LetterListResponseDto> letters;

    private UserDto writer;


    @Builder
    private LetterListPagenationResponseDto(int size, int currentPage, int totalPages, long totalItems,
                                            List<LetterListResponseDto> letters, UserDto writer) {
        this.size = size;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.letters = letters;
        this.writer = writer;
    }

    public static LetterListPagenationResponseDto createResponseDto(int size, int currentPage, int totalPages, long totalItems,
                                                                    List<LetterListResponseDto> letters, UserDto writer) {
        return LetterListPagenationResponseDto.builder()
                .size(size)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .letters(letters)
                .writer(writer)
                .build();
    }

}
