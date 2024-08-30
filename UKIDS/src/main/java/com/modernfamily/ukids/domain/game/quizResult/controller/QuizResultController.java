package com.modernfamily.ukids.domain.game.quizResult.controller;

import com.modernfamily.ukids.domain.game.quizResult.model.service.QuizResultService;
import com.modernfamily.ukids.global.util.HttpMethodCode;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizResultController {

    private final HttpResponseUtil responseUtil;
    private final QuizResultService quizResultService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getQuizResult(@RequestParam(value = "size", defaultValue = "5") int size,
                                                             @RequestParam(value = "page", defaultValue = "1") int page) {
        return responseUtil.createResponse(HttpMethodCode.GET, quizResultService.getQuizResults(size, page));
    }

}
