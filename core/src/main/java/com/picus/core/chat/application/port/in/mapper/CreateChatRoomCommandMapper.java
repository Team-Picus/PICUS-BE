package com.picus.core.chat.application.port.in.mapper;

import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import com.picus.core.chat.domain.model.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class CreateChatRoomCommandMapper {

    public ChatRoom toDomain(CreateChatRoomCommand command) {
        return ChatRoom.builder()
                .clientNo(command.clientNo())
                .expertNo(command.expertNo())
                .isPinned(false)
                .build();
    }
}
