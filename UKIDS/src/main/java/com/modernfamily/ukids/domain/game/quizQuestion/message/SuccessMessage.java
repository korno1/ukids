package com.modernfamily.ukids.domain.game.quizQuestion.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {

    SUCCESS_CREATE_QUIZ_QUESTION("퀴즈 생성 완료"),
    SUCCESS_UPDATE_QUIZ_QUESTION("퀴즈 수정 완료"),
    SUCCESS_DELETE_QUIZ_QUESTION("퀴즈 삭제 완료");

    private String message;


}