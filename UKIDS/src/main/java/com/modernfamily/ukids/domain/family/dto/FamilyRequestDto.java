package com.modernfamily.ukids.domain.family.dto;

import lombok.Data;

@Data
public class FamilyRequestDto {
    private String name;
    private String code;
    private String password;

    private Long representative;
}
