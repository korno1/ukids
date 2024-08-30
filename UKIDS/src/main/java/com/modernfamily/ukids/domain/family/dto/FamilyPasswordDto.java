package com.modernfamily.ukids.domain.family.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FamilyPasswordDto {
    private Long familyId;
    private String password;
}
