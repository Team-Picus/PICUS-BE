package com.picus.core.chat.adapter.in.web.data.request;

import com.picus.core.chat.domain.model.vo.MessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SendFileMessageRequest(
        @NotBlank String fileKey
) implements SendMessageRequest{
    @Override
    public MessageType type() {
        return MessageType.FILE;
    }
}
