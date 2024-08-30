package com.modernfamily.ukids.domain.photo.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {

    SUCCESS_SAVE_PHOTO("사진 저장 완료"),
    SUCCESS_DELETE_PHOTO("사진 삭제 완료");

    private String message;


}
