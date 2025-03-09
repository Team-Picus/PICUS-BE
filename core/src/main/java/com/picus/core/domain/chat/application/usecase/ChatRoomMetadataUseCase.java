package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomMetadataUseCase {

    private final ChatRoomService chatRoomService;

    public Long initRoom(Long clientNo, Long expertNo) {
        return chatRoomService
                .create(clientNo, expertNo)
                .getId();
    }

    public Page<ChatRoomRes> readChatRooms(UserPrincipal userPrincipal, Integer page) {
        return chatRoomService.findMyRooms(userPrincipal.getUserId(), page);
    }

    public void leaveChatRoom(Long userNo, Long roomNo) {
        chatRoomService.delete(userNo, roomNo);
    }
}
