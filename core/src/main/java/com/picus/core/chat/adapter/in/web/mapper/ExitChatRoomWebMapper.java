package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import org.springframework.stereotype.Component;

@Component
public class ExitChatRoomWebMapper {

    public ExitChatRoomCommand toCommand(ExitChatRoomRequest request, String currentUserNo) {
        return ExitChatRoomCommand.builder()
                .chatRoomNos(request.chatRoomNos())
                .currentUserNo(currentUserNo)
                .build();
    }
}
