package com.modernfamily.ukids.domain.growthRecord.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    SUCCESS_DELETE_GROWTHRECORD("자녀성장일지 삭제 완료"),
    SUCCESS_UPDATE_GROWTHRECORD("자녀성장일지 수정 완료"),
    SUCCESS_CREATE_GROWTHRECORD("자녀성장일지 생성 완료");


    private final String message;



}
