package com.modernfamily.ukids.domain.chatRoom.model.repository;

import com.modernfamily.ukids.domain.chatMessage.pubsub.RedisSubscriber;
import com.modernfamily.ukids.domain.chatRoom.entity.ChatRoom;
import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.family.mapper.FamilyMapper;
import com.modernfamily.ukids.domain.family.model.repository.FamilyRepository;
import com.modernfamily.ukids.domain.user.mapper.UserMapper;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    // 채팅방(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을 수 있도록 한다.
    private Map<String, ChannelTopic> topics;
    private final FamilyRepository familyRepository;
    private final FamilyMapper familyMapper;
    private final UserMapper userMapper;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomByFamilyId(Long familyId) {
        return opsHashChatRoom.get(CHAT_ROOMS, familyId);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash 에 저장한다.
     */
    public ChatRoom createChatRoom(Long familyId) {
        Family family = familyRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FAMILY_EXCEPTION));

        FamilyResponseDto familyResponseDto = familyMapper.toFamilyResponseDto(family);
        familyResponseDto.setUserFamilyDto(userMapper.toUserFamilyDto(family.getUser()));


        ChatRoom chatRoom = ChatRoom.create(familyId, family.getName(), familyId, familyResponseDto);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getChatRoomId().toString(), chatRoom);
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(Long roomId) {
        ChannelTopic topic = topics.get(roomId.toString());
        if (topic == null) {
            topic = new ChannelTopic(roomId.toString());
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId.toString(), topic);
        }
    }

    /**
     * 채팅방 나가기 : Redis에서 해당 채팅방의 topic을 제거한다.
     */
    public void exitChatRoom(Long roomId) {
        ChannelTopic topic = topics.get(roomId.toString());
        if (topic != null) {
            redisMessageListener.removeMessageListener(redisSubscriber, topic);
            topics.remove(roomId.toString());
        }
    }

    public ChannelTopic getTopic(Long roomId) {
        return topics.get(roomId.toString());
    }
}
