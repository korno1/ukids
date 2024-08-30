package com.modernfamily.ukids.domain.pictureDiary.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    SUCCESS_CREATE_PICTUREDIARY("그림일기 생성 완료"),
    SUCCESS_DELETE_PICTUREDIARY("그림일기 삭제 완료"),
    SUCCESS_UPDATE_PICTUREDIARY("그림일기 수정 완료");



    private final String message;



}
