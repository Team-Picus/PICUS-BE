package com.picus.core.chat.application.port.in.result;

import com.picus.core.chat.domain.model.vo.MessageType;

public record CreateReservationMessageResult(
        String senderNo
) implements CreateMessageResult {
    @Override
    public MessageType type() {
        return MessageType.RESERVATION;
    }
}
