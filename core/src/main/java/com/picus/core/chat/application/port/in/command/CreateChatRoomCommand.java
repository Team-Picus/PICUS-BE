package com.picus.core.chat.application.port.in.command;

import lombok.Builder;

@Builder
public record CreateChatRoomCommand(
        String clientNo,
        String expertNo
) {
}
