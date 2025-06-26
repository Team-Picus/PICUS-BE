package com.picus.core.old.domain.chat.domain.service;

import com.picus.core.old.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.old.domain.chat.domain.entity.ChatRoom;
import com.picus.core.old.domain.chat.domain.repository.ChatRoomRepository;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Boolean isExist(Long roomNo) {
        return chatRoomRepository.existsById(roomNo);
    }

    public ChatRoom create() {
        return chatRoomRepository.save(new ChatRoom());
    }

    @Transactional
    public void updateLastMessage(Long roomNo, String lastMessage) {
        ChatRoom chatRoom = findByRoomNo(roomNo);
        chatRoom.updateLastMessage(lastMessage);
    }

    public ChatRoom findByRoomNo(Long roomNo) {
        return chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    public List<ChatRoomRes> findMyRoomsMetadata(Long userNo, Long lastMessageNo) {
        return chatRoomRepository.findMyChatRoomMetadata(userNo, lastMessageNo);
    }

}
