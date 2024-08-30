package com.modernfamily.ukids.domain.chatRoom.model.service;

import com.modernfamily.ukids.domain.chatRoom.entity.ChatRoom;
import com.modernfamily.ukids.domain.chatRoom.model.repository.ChatRoomRepository;
import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.global.validation.FamilyMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final FamilyMemberValidator familyMemberValidator;

    // 채팅방 생성
    @Override
    public ChatRoom createFamilyChatRoom(Long familyId) {
        // 이미 존재하는 ChatRoom 인지 확인
        ChatRoom existingChatRoom = chatRoomRepository.findRoomByFamilyId(familyId);

        // 만약 기존 채팅방이 없다면 새 채팅방 생성
        if (existingChatRoom == null) {
            ChatRoom newChatRoom = chatRoomRepository.createChatRoom(familyId);
            return newChatRoom;
        }

        // 기존 채팅방이 존재하면 기존 채팅방 반환
        return existingChatRoom;
    }

    // 전체 채팅방 조회
    @Override
    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAllRoom();
    }

    // 가족 ID를 통한 채팅방 조회
    @Override
    public ChatRoom findRoomByRoomId(Long familyId) {
        Family validFamily = familyMemberValidator.checkUserInFamilyMember(familyId).getFamily();

        ChatRoom chatRoom = chatRoomRepository.findRoomByFamilyId(validFamily.getFamilyId());

        return chatRoom;
    }

    // 현재 로그인한 유저가 가족멤버인지 확인
    @Override
    public Family validateFamilyMember(Long familyId) {
        Family family = familyMemberValidator.checkUserInFamilyMember(familyId).getFamily();

        return family;
    }

}
