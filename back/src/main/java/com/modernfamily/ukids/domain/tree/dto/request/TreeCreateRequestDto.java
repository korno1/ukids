package com.modernfamily.ukids.domain.tree.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TreeCreateRequestDto {

    @NotNull
    private Long familyId;
}
