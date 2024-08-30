package com.modernfamily.ukids.domain.growthFolder.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    SUCCESS_DELETE_GROWTHFOLDER("자녀성장일지 폴더 삭제 완료"),
    SUCCESS_CREATE_GROWTHFOLDER("자녀성장일지 폴더 생성 완료"),
    SUCCESS_UPDATE_GROWTHFOLDER("자녀성장일지 폴더 수정 완료");


    private final String message;



}
