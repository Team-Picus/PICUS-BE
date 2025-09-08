package com.picus.core.chat.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadMyChatRoomListResponse(
        List<ChatRoomResponse> chatRooms
) {
    @Builder
    public record ChatRoomResponse(
            String peerNickname,
            String peerProfileUrl,
            String lastMessage,
            LocalDateTime lastMessageSentAt,
            boolean isPinned,
            long unreadMessageCount
    ) {

    }
}
