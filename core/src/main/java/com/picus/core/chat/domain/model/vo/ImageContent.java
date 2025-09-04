package com.picus.core.chat.domain.model.vo;

import lombok.Builder;

@Builder
public record ImageContent (
        String imageKey
) implements MessageContent {
}
