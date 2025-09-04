package com.picus.core.chat.adapter.in.web.data.response;

import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Builder;

@Builder
public record CreateReservationMessageResponse(
        String senderNo
) implements CreateMessageResponse {
    @Override
    public MessageType type() {
        return MessageType.RESERVATION;
    }
}
