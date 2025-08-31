package com.picus.core.chat.application.port.in.command;

import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;

public sealed interface CreateMessageCommand
        permits CreateTextMessageCommand, CreateImageMessageCommand, CreateFileMessageCommand, CreateReservationMessageCommand {
    MessageContent content();
    MessageType type();
    String chatRoomNo();
    String senderNo();
}
