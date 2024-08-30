package com.modernfamily.ukids.domain.user.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    SUCCESS_CREATE_USER("회원 생성 완료"),
    SUCCESS_UPDATE_USER("회원 수정 완료"),
    SUCCESS_ID_EXIST("id 중복"),
    SUCCESS_ID_NOT_EXIST("id 중복 없음"),
    SUCCESS_EMAIL_EXIST("email 중복"),
    SUCCESS_EMAIL_NOT_EXIST("email 중복 없음"),
    SUCCESS_PHONE_EXIST("phone 중복"),
    SUCCESS_PHONE_NOT_EXIST("phone 중복 없음"),
    SUCCESS_PASSWORD_SAME("비밀번호 일치"),
    SUCCESS_PASSWORD_DISCORD("비밀번호 불일치");


    private final String message;



}
