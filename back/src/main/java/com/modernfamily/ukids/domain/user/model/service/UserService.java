package com.modernfamily.ukids.domain.user.model.service;

import com.modernfamily.ukids.domain.user.dto.*;
import com.modernfamily.ukids.domain.user.entity.User;

public interface UserService {

    String login(UserLoginDto userLoginDto);

    String updateUser(UserUpdateDto userUpdateDto);

    void signUp(SignUpDto signUpDto);

    boolean idExist(String id);
    boolean emailExist(String email);
    boolean phoneExist(String phone);

    UserDto getUser(Long userId);

    boolean pwCheck(PasswordCheckDto userDto);

    UserOtherDto findByIdOther(String id);
}
