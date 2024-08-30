package com.modernfamily.ukids.domain.familyMember.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {
    SUCCESS_APPLY_FAMILY_MEMBER("구성원 신청 완료"),
    SUCCESS_APPROVE_FAMILY_MEMBER("구성원 신청 승인 완료"),
    SUCCESS_CANCEL_FAMILY_MEMBER("구성원 신청 취소 완료"),
    SUCCESS_DENY_FAMILY_MEMBER("구성원 신청 거절 완료"),
    SUCCESS_ROLE_FAMILY_MEMBER("구성원 역할 변경 완료"),
    SUCCESS_DELETE_MEMBER("구성원 탈퇴 완료");



    private final String message;



}
