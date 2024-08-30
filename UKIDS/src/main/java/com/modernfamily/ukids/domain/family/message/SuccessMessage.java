package com.modernfamily.ukids.domain.family.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    SUCCESS_CREATE_FAMILY("가족 생성 완료"),
    SUCCESS_UPDATE_FAMILY("가족 수정 완료"),
    SUCCESS_DELETE_FAMILY("가족 삭제 완료"),
    SUCCESS_PASSWORD_SAME("비밀번호 일치"),
    SUCCESS_PASSWORD_DISCORD("비밀번호 불일치");


    private final String message;



}
