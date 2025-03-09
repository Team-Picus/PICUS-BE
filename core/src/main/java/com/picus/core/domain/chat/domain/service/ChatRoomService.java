package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.repository.ChatRoomRepository;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Boolean isExist(Long roomNo) {
        return chatRoomRepository.existsById(roomNo);
    }

    // todo: 파라미터 수정
    public ChatRoom create(Long clientNo, Long exportNo) {
        return chatRoomRepository.save(new ChatRoom());
    }

    @Transactional
    public void updateLastMessage(Long roomNo, String lastMessage) {
        ChatRoom chatRoom = findByRoomNo(roomNo);
        chatRoom.updateLastMessage(lastMessage);
    }

    // todo: 엔티티 매핑 변경
    public Page<ChatRoomRes> findMyRooms(Long userNo, Integer page) {
//        return chatRoomRepository.findChatRooms(userNo, PageRequest.of(page, 20));
        return null;
    }

    public ChatRoom findByRoomNo(Long roomNo) {
        return chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    @Transactional
    public void delete(Long userNo, Long roomNo) {
        ChatRoom chatRoom = findByRoomNo(roomNo);
//        chatRoom.leave(userNo);
    }

}
