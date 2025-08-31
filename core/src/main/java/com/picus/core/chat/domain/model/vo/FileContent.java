package com.picus.core.chat.domain.model.vo;

import lombok.Builder;

@Builder
public record FileContent(
    String fileKey
) implements MessageContent {
}
