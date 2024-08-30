package com.modernfamily.ukids.domain.letter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LetterCreateRequestDto {

    @NotNull
    private Long familyId;

    @NotBlank
    private String content;

    @NotNull
    private Long toUserId;
}
