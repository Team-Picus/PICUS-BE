package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;

public interface CreateChatRoomUseCase {
    String create(CreateChatRoomCommand command);
}
