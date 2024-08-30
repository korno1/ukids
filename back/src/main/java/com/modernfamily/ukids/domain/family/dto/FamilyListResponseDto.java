package com.modernfamily.ukids.domain.family.dto;

import lombok.Data;

@Data
public class FamilyListResponseDto {
    private Long familyId;
    private String name;
    private Long representative;
}
