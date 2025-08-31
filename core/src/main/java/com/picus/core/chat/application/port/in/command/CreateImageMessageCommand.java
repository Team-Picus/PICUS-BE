package com.picus.core.chat.application.port.in.command;

import com.picus.core.chat.domain.model.vo.ImageContent;
import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Builder;

@Builder
public record CreateImageMessageCommand(
        String chatRoomNo,
        String senderNo,
        ImageContent imageContent
) implements CreateMessageCommand {
    @Override
    public MessageContent content() {
        return imageContent;
    }

    @Override
    public MessageType type() {
        return MessageType.IMAGE;
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
