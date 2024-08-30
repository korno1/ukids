package com.modernfamily.ukids.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernfamily.ukids.global.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker // 메시지 브로커가 지원하는 WebSocket 메시지 처리를 활성화
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtConfig jwtConfig;

    // HandShake 와 통신을 담당할 EndPoint 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 메모리 기반의 Simple Message Broker 활성화
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커가 Subscriber들에게 메시지를 전달할 URL 지정 (메시지 구독 요청)
        config.enableSimpleBroker("/sub", "/topic");
        // 클라이언트가 서버로 메시지 보낼 URL 접두사 지정 (메시지 발행 요청)
        config.setApplicationDestinationPrefixes("/pub", "/app");
    }
    
    // STOMP 연결 시, 호출
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.info("Registering client inbound channel");

        registration.interceptors(new StompHandler(jwtConfig));
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ErrorHandlingInterceptor());
    }

    private static class ErrorHandlingInterceptor implements ChannelInterceptor {
        private final ObjectMapper objectMapper = new ObjectMapper();

        public void afterSendCompletion(Message<?> message, MessageChannel channel, Exception ex) {
            if (ex != null) {
                String errorMessage;
                try {
                    errorMessage = objectMapper.writeValueAsString(new ErrorMessage("ERROR", ex.getMessage()));
                } catch (Exception e) {
                    errorMessage = "{\"type\": \"ERROR\", \"message\": \"Unknown error\"}";
                }

                log.error("Sending error message to clients: {}", errorMessage);
            }
        }
    }

    private static class ErrorMessage {
        private String type;
        private String message;

        public ErrorMessage(String type, String message) {
            this.type = type;
            this.message = message;
        }
    }
}
