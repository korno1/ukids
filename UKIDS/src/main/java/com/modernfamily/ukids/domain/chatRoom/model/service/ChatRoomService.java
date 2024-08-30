package com.modernfamily.ukids.domain.chatRoom.model.service;

import com.modernfamily.ukids.domain.chatRoom.entity.ChatRoom;
import com.modernfamily.ukids.domain.family.entity.Family;

import java.util.List;

public interface ChatRoomService {

    ChatRoom createFamilyChatRoom(Long familyId);

    List<ChatRoom> findAllRoom();

    ChatRoom findRoomByRoomId(Long familyId);

    Family validateFamilyMember(Long familyId);


}
