package com.picus.core.chat.domain.model.vo;

import lombok.Builder;

@Builder
public record TextContent(
        String text
) implements MessageContent {
}
