package com.modernfamily.ukids.domain.family.dto;

import lombok.Data;

@Data
public class FamilyUpdateDto {
    private Long familyId;
    private String name;
    private String password;
    private Long representative;
}
