package com.picus.core.chat.application.port.in.command;

import com.picus.core.chat.domain.model.vo.FileContent;
import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Builder;

@Builder
public record CreateFileMessageCommand(
        String chatRoomNo,
        String senderNo,
        FileContent fileContent
) implements CreateMessageCommand {
    @Override
    public MessageContent content() {
        return fileContent;
    }

    @Override
    public MessageType type() {
        return MessageType.FILE;
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
