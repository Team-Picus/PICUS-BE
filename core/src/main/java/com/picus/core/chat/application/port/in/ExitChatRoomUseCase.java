package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;

import java.util.List;

public interface ExitChatRoomUseCase {
    void exit(ExitChatRoomCommand command);
}
