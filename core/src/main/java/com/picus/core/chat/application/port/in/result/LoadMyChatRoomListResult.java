package com.picus.core.chat.application.port.in.result;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadMyChatRoomListResult(
        String peerNickname,
        String peerProfileUrl,
        String lastMessage,
        LocalDateTime lastMessageSentAt,
        boolean isPinned,
        long unreadMessageCount
) {
}
