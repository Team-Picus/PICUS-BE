package com.picus.core.chat.adapter.in.web.data.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.picus.core.chat.domain.model.vo.MessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonTypeName("TEXT")
public record SendTextMessageRequest(
        @NotBlank String text
) implements SendMessageRequest{
    @Override
    public MessageType type() {
        return MessageType.TEXT;
    }
}
