package com.picus.core.chat.adapter.in.web.data.request;

import com.picus.core.chat.domain.model.vo.MessageType;

public record SendFileMessageRequest(
        String fileKey
) implements SendMessageRequest{
    @Override
    public MessageType type() {
        return MessageType.FILE;
    }
}
