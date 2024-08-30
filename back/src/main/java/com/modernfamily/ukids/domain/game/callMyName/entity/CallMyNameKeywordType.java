package com.modernfamily.ukids.domain.game.callMyName.entity;

import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallMyNameKeywordType {

    IDOL("아이돌"), ACTOR("배우"), HISTORY("역사인물");

    private String title;

    // title 값을 통해 CallMyNameKeywordType 찾기
    public static CallMyNameKeywordType findByTitle(String title) {
        for (CallMyNameKeywordType type : CallMyNameKeywordType.values()) {
            if (type.getTitle().equals(title)) {
                return type;
            }
        }
        throw new ExceptionResponse(CustomException.NOT_FOUND_KEYWORD_TYPE_EXCEPTION);
    }
}
