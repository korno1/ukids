package com.modernfamily.ukids.domain.user.dto;

import lombok.Data;

@Data
public class UserFamilyDto {
    private Long userId;
    private String id;
    private String name;
    private String email;
    private String phone;
    private String birthDate;
    private String profileImage;
}
