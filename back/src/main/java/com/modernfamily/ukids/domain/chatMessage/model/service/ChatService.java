package com.modernfamily.ukids.domain.chatMessage.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernfamily.ukids.domain.chatMessage.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<ChatMessage> getChatMessages(String roomId) {
        String key = "chat:messages:" + roomId;
        List<Object> rawMessages = redisTemplate.opsForList().range(key, 0, -1);
        return rawMessages.stream()
                .map(message -> {
                    try {
                        // rawMessages의 각 요소가 LinkedHashMap으로 변환되었을 수 있으므로,
                        // 이를 ChatMessage로 변환하는 방법 수정
                        if (message instanceof LinkedHashMap) {
                            return objectMapper.convertValue(message, ChatMessage.class);
                        } else if (message instanceof String) {
                            return objectMapper.readValue((String) message, ChatMessage.class);
                        } else {
                            throw new RuntimeException("Unexpected message type: " + message.getClass());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
