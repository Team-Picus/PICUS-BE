package com.picus.core.chat.adapter.in.web.data.request;

import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Builder;

@Builder
public record SendReservationMessageRequest(
) implements SendMessageRequest{
    @Override
    public MessageType type() {
        return MessageType.RESERVATION;
    }
}
