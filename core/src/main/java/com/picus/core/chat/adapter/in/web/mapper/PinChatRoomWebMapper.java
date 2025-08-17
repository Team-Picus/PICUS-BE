package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.adapter.in.web.data.request.PinChatRoomRequest;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;
import org.springframework.stereotype.Component;

@Component
public class PinChatRoomWebMapper {

    public PinChatRoomCommand toCommand(PinChatRoomRequest request, String currentUserNo) {
        return PinChatRoomCommand.builder()
                .chatRoomNos(request.chatRoomNos())
                .currentUserNo(currentUserNo)
                .build();
    }
}

