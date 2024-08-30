package com.modernfamily.ukids.domain.game.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernfamily.ukids.domain.game.chatgpt.dto.Chatgpt;
import com.modernfamily.ukids.global.config.ChatgptConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatgptService {
    private final ChatgptConfig chatGPTConfig;

    @Value("${chatgpt.open-api.model}")
    private String model;

    public List<String> runPrompt(String prompt) {
        log.debug("[+] 프롬프트를 수행합니다.");

        List<Map<String, Object>> result = null;

        HttpHeaders headers = chatGPTConfig.httpHeaders();

        String requestBody;
        ObjectMapper om = new ObjectMapper();

        Chatgpt gpt = Chatgpt.createChatgpt(model, prompt, 0.0f, 1000);

        try {
            requestBody = om.writeValueAsString(gpt);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = chatGPTConfig.restTemplate()
                .exchange(
                        "https://api.openai.com/v1/completions",
                        HttpMethod.POST,
                        requestEntity,
                        String.class);

        try {
            Map<String, Object> responseMap = om.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            result = (List<Map<String, Object>>) responseMap.get("choices");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println((String)result.get(0).get("text"));
        String text = (String)result.get(0).get("text");
        return getText(text);
    }

    private List<String> getText(String text) {
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+\\.\\s+(.*?)($|\\n)");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            resultList.add(matcher.group(1));
        }

        return resultList;
    }
}
