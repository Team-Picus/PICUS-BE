package com.picus.core.chat.application.port.in.command;

import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import com.picus.core.chat.domain.model.vo.ReservationContent;
import lombok.Builder;

@Builder
public record CreateReservationMessageCommand(
        String chatRoomNo,
        String senderNo,
        ReservationContent reservationContent
) implements CreateMessageCommand {
    @Override
    public MessageContent content() {
        return reservationContent;
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
