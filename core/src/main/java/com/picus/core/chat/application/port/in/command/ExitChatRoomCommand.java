package com.picus.core.chat.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record ExitChatRoomCommand(
        List<String> chatRoomNos,
        String currentUserNo
) {
}
