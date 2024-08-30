package com.modernfamily.ukids.domain.user.dto;

import com.modernfamily.ukids.domain.user.entity.Role;
import lombok.Data;

@Data
public class SignUpDto {

    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String birthDate;
    private String profileImage;
    private Role role;


}
