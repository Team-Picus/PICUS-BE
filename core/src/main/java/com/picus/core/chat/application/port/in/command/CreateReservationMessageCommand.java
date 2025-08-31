package com.picus.core.chat.application.port.in.command;

import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;

public record CreateReservationMessageCommand(
        String chatRoomNo,
        String senderNo
) implements CreateMessageCommand {
    @Override
    public MessageContent content() {
        return null;
    }

    @Override
    public MessageType type() {
        return MessageType.RESERVATION;
    }

    @Override
    public String chatRoomNo() {
        return chatRoomNo;
    }

    @Override
    public String senderNo() {
        return senderNo;
    }
}
