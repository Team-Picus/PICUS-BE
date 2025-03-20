package com.picus.core.domain.chat.application.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomRes (

        // ChatRoom Metadata
        Long roomNo,
        String lastMessageAt,
        String thumbnailMessage,
        Integer unreadMessageCnt,

        // Message Partner
        Long partnerId,
        Long profileImageId,
        String profileImageUrl,
        String nickname

) {}
