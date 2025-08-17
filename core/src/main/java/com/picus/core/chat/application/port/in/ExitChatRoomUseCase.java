package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;

public interface ExitChatRoomUseCase {
    void exit(ExitChatRoomCommand command);
}
