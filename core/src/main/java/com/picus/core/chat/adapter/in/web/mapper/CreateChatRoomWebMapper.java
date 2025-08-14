package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.CreateChatRoomRequest;
import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import org.springframework.stereotype.Component;

@Component
public class CreateChatRoomWebMapper {
    public CreateChatRoomCommand toCommand(String clientNo, CreateChatRoomRequest request) {
        return CreateChatRoomCommand.builder()
                .clientNo(clientNo)
                .expertNo(request.expertNo())
                .build();
    }
}
