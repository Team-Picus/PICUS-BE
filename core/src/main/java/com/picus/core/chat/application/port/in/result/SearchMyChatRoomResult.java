package com.picus.core.chat.application.port.in.result;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchMyChatRoomResult(
        String peerNickname,
        String peerProfileUrl,
        String matchingMessage,
        LocalDateTime matchingMessageSentAt,
        boolean isPinned,
        long unreadMessageCount
) {
}
