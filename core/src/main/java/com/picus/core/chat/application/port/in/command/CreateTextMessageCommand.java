package com.picus.core.chat.application.port.in.command;

import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import com.picus.core.chat.domain.model.vo.TextContent;
import lombok.Builder;

@Builder
public record CreateTextMessageCommand(
        String chatRoomNo,
        String senderNo,
        TextContent textContent
) implements CreateMessageCommand {
    @Override
    public MessageContent content() {
        return textContent;
    }

    @Override
    public MessageType type() {
        return MessageType.TEXT;
    }

    @Override
    public String chatRoomNo() {
        return chatRoomNo;
    }

    @Override
    public String senderNo() {
        return senderNo;
    }
}
