package com.modernfamily.ukids.domain.tree.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TreeUpdateRequestDto {

    @NotNull
    private Long familyId;

    @NotNull
    private Long point;

}
