package com.picus.core.domain.chat.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomRes (

        // ChatRoom Metadata
        Long roomNo,
        LocalDateTime lastMessageAt,
        String thumbnailMessage,
        Integer unreadMessageCnt,

        // Message Partner
        Long partnerId,
        Long profileImageId,
        String profileImageUrl,
        String nickname

) {}
