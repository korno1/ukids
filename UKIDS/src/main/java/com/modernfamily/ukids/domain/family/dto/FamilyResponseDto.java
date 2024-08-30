package com.modernfamily.ukids.domain.family.dto;

import com.modernfamily.ukids.domain.user.dto.UserFamilyDto;
import lombok.Data;

@Data
public class FamilyResponseDto {
    private Long familyId;
    private String name;
    private String code;
    private UserFamilyDto userFamilyDto;
}
