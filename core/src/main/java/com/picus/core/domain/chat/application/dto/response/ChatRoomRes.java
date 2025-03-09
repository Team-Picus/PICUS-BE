package com.picus.core.domain.chat.application.dto.response;

public record ChatRoomRes (
        Long roomNo,
        Long clientNo,
        Long expertNo,
        String lastMessageAt,
        String thumbnailMessage
) {}
