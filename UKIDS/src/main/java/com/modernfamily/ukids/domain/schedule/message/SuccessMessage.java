package com.modernfamily.ukids.domain.schedule.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    
    SUCCESS_CREATE_SCHDULE("일정 등록 성공"),
    SUCCESS_UPDATE_SCHDULE("일정 수정 성공"),
    SUCCESS_DELETE_SCHDULE("일정 삭제 성공");

    private String message;

}
