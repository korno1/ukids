package com.modernfamily.ukids.domain.game.callMyName.model.service;

import com.modernfamily.ukids.domain.game.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallMyNameKeywordService {

    private final ChatgptService chatgptService;

    // KeywordType에 따른 Keyword 생성 (Chatgpt)
    public List<String> generateCallMyNameKeyword(String keywordType, int participateSize) {

        // KeywordType에 따라 Keyword를 생성
        List<String> keywords = new ArrayList<>();
        String prompt = "초등학생이 알만한 " + keywordType + " " + participateSize + "명 이름만 띄어쓰기로 구분해서 알려줘.";
        // 예시: 지민 정국 진 슈가
        // 해당 결과값이 List 에 잘 들어가는지 모르겠음
        List<String> chatgptAnswer = chatgptService.runPrompt(prompt);

        keywords.addAll(chatgptAnswer);

        return keywords;
    }

}
