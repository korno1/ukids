package com.modernfamily.ukids.domain.chatMessage.controller;

import com.modernfamily.ukids.domain.chatMessage.entity.ChatMessage;
import com.modernfamily.ukids.domain.chatMessage.pubsub.RedisPublisher;
import com.modernfamily.ukids.domain.chatRoom.model.repository.ChatRoomRepository;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.domain.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Optional;

// Publisher 구현
@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();


        log.info("########## userId : {} ############", userId);
        Optional<User> existUser = userRepository.findById(userId);

        if (existUser.isEmpty() || existUser == null) {
            log.info("########## exist user : {} ############", existUser);
            return;
        }

        User loginUser = existUser.get();

        message.setSenderId(loginUser.getUserId());
        message.setSender(loginUser.getName());

        log.info("****chatMessage**** : {}", message.getType());

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {

            chatRoomRepository.enterChatRoom(message.getRoomId());
//            message.setMessage(message.getSender() + " 님이 입장하셨습니다.");
        } else {
            // WebSocket에 발행된 메시지를 redis로 발행한다
            redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
        }
        Long roomId = message.getRoomId();
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    /**
     * websocket "/pub/chat/leave"로 들어오는 채팅방 나가기 메시징을 처리한다.
     */
    @MessageMapping("/chat/leave")
    public void leave(ChatMessage message) {
        if (ChatMessage.MessageType.EXIT.equals(message.getType())) {
            chatRoomRepository.exitChatRoom(message.getRoomId());
        }
    }
}
