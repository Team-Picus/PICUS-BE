package com.picus.core.chat.application.port.in.result;

import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Builder;

@Builder
public record CreateImageMessageResult(
        String senderNo,
        String imageUrl
) implements CreateMessageResult {
    @Override
    public MessageType type() {
        return MessageType.IMAGE;
    }
}
