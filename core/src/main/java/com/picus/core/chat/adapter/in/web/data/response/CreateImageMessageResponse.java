package com.picus.core.chat.adapter.in.web.data.response;

import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Builder;

@Builder
public record CreateImageMessageResponse(
        String senderNo,
        String imageUrl
) implements CreateMessageResponse {
    @Override
    public MessageType type() {
        return MessageType.IMAGE;
    }
}
