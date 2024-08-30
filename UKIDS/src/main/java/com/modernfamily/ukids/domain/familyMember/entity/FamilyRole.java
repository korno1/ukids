package com.modernfamily.ukids.domain.familyMember.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum FamilyRole {
    ROLE_FATHER("아빠"),
    ROLE_MOTHER("엄마"),
    ROLE_GRANDFATHER("할아버지"),
    ROLE_GRANDMOTHER("할머니"),
    ROLE_SON("아들"),
    ROLE_DAUGHTER("딸"),
    ROLE_NONE("없음");

    private final String roleName;


}
