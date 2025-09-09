package com.picus.core.chat.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SearchMyChatRoomResponse(
        List<ChatRoomResponse> chatRooms
) {
    @Builder
    public record ChatRoomResponse(
            String peerNickname,
            String peerProfileUrl,
            String matchingMessage,
            LocalDateTime matchingMessageSentAt,
            boolean isPinned,
            long unreadMessageCount
    ) {

    }
}
