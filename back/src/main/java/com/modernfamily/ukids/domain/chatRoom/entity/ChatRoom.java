package com.modernfamily.ukids.domain.chatRoom.entity;

import com.modernfamily.ukids.domain.family.dto.FamilyResponseDto;
import com.modernfamily.ukids.domain.family.entity.Family;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChatRoom {

    private static final long serialVersionUID = 1L;

    @Id
    private Long chatRoomId;

    private String chatRoomName;

    private Long familyId;

    @OneToOne
    private FamilyResponseDto family;

    public ChatRoom() {}

    public static ChatRoom create(Long chatRoomId, String chatRoomName, Long familyId, FamilyResponseDto family) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = chatRoomId;
        chatRoom.chatRoomName = chatRoomName;
        chatRoom.familyId = familyId;
        chatRoom.family = family;
        return chatRoom;
    }

}
