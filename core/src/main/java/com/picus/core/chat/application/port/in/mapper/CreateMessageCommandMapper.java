package com.picus.core.chat.application.port.in.mapper;

import com.picus.core.chat.application.port.in.command.CreateMessageCommand;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.MessageReadStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateMessageCommandMapper {

    public Message toDomain(CreateMessageCommand messageCommand, List<MessageReadStatus> messageReadStatuses) {
        return Message.builder()
                .chatRoomNo(messageCommand.chatRoomNo())
                .senderNo(messageCommand.senderNo())
                .messageType(messageCommand.type())
                .content(messageCommand.content())
                .messageReadStatuses(messageReadStatuses)
                .build();
    }
}
