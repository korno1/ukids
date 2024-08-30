package com.modernfamily.ukids.domain.game.quizQuestion.controller;

import com.modernfamily.ukids.domain.game.quizQuestion.dto.request.QuizQuestionCreateRequestDto;
import com.modernfamily.ukids.domain.game.quizQuestion.dto.request.QuizQuestionUpdateRequestDto;
import com.modernfamily.ukids.domain.game.quizQuestion.message.SuccessMessage;
import com.modernfamily.ukids.domain.game.quizQuestion.model.service.QuizQuestionService;
import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/quiz-question")
@RequiredArgsConstructor
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;
    private final HttpResponseUtil responseUtil;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createQuizQuestion(@RequestBody QuizQuestionCreateRequestDto requestDto) {
        quizQuestionService.createQuizQuestion(requestDto);

        return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_QUIZ_QUESTION.getMessage());
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateQuizQuestion(@RequestBody QuizQuestionUpdateRequestDto requestDto) {
        quizQuestionService.updateQuizQuestion(requestDto);

        return responseUtil.createResponse(HttpMethodCode.PUT, SuccessMessage.SUCCESS_UPDATE_QUIZ_QUESTION.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteQuizQuestion(@PathVariable("id") Long quizQuestionId) {
        quizQuestionService.deleteQuizQuestion(quizQuestionId);

        return responseUtil.createResponse(HttpMethodCode.DELETE, SuccessMessage.SUCCESS_DELETE_QUIZ_QUESTION.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getQuizQuestion(@PathVariable("id") Long quizQuestionId) {

        return responseUtil.createResponse(HttpMethodCode.GET,
                quizQuestionService.getQuizQuestion(quizQuestionId));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getQuizQuestionListByUser(@RequestParam(value = "size", defaultValue = "5") int size,
                                                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        return responseUtil.createResponse(HttpMethodCode.GET,
                quizQuestionService.getQuizQuestionListByUser(size, page));
    }

    @GetMapping("/random/{id}")
    public ResponseEntity<Map<String, Object>> getQuizQuestionListByUser(@PathVariable("id") String userId,
                                                                         @RequestParam("count") long count) {
        return responseUtil.createResponse(HttpMethodCode.GET,
                quizQuestionService.chooseRandomQuizQuestion(userId, count));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCountQuizQuestionByUser() {
        return responseUtil.createResponse(HttpMethodCode.GET,
                quizQuestionService.getCountQuizQuestionByUser(CustomUserDetails.contextGetUserId()));
    }

}
