package com.modernfamily.ukids.domain.user.dto;

import com.modernfamily.ukids.domain.user.entity.Role;
import lombok.Data;

@Data
public class JwtUserDto {
    private Long userId;
    private String id;
    private String name;
    private String email;
    private String phone;
    private String birthDate;
    private Role role;
}
