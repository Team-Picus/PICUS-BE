package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.repository.ChatRoomRepository;
import com.picus.core.domain.chat.domain.repository.ChatUserRepository;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

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
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public List<ChatRoomRes> findMyRoomsMetadata(Long userNo, Long lastMessageNo) {
        return chatRoomRepository.findMyChatRoomMetadata(userNo, lastMessageNo);
    }

}
