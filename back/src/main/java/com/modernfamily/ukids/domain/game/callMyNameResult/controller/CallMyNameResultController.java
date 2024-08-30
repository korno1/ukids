package com.modernfamily.ukids.domain.game.callMyNameResult.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/call")
@RequiredArgsConstructor
public class CallMyNameResultController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> callMyNameResult(@RequestParam(value = "size", defaultValue = "5") int size,
                                                                @RequestParam(value = "page", defaultValue = "1") int page) {
        return null;
    }
}
