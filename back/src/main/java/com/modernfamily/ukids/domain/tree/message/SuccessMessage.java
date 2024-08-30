package com.modernfamily.ukids.domain.tree.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {

    SUCCESS_CREATE_TREE("나무 생성 완료"),
    SUCCESS_UPDATE_TREE("나무 수정 완료"),
    ;

    private String message;
}
