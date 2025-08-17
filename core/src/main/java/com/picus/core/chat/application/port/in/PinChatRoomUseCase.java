package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;

public interface PinChatRoomUseCase {
    void pin(PinChatRoomCommand command);
}
