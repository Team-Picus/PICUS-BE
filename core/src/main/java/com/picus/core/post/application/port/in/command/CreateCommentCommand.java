package com.picus.core.post.application.port.in.command;

import lombok.Builder;

@Builder
public record CreateCommentCommand(
        String postNo,
        String authorNo,
        String content
) {
}
