package com.picus.core.chat.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record PinChatRoomCommand(
        List<String> chatRoomNos,
        String currentUserNo
) {
}
