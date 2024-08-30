package com.modernfamily.ukids.domain.game.chatgpt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatgpt {

    private String model;

    private String prompt;

    private float temperature;

    private int max_tokens;

    @Builder
    Chatgpt(String model, String prompt, float temperature, int max_tokens) {
        this.model = model;
        this.prompt = prompt;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }

    public static Chatgpt createChatgpt(String model, String prompt, float temperature, int max_tokens) {
        return Chatgpt.builder()
                .model(model)
                .prompt(prompt)
                .temperature(temperature)
                .max_tokens(max_tokens)
                .build();
    }
}
