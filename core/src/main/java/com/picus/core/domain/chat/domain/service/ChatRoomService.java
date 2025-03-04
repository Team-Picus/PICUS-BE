package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Boolean isExist(Long roomNo) {
        return chatRoomRepository.existsById(roomNo);
    }

    public ChatRoom create(Long clientNo, Long exportNo) {
        return chatRoomRepository.save(new ChatRoom(clientNo, exportNo));
    }

    public void updateLastMessage(Long roomNo, String lastMessage) {

    }
}
