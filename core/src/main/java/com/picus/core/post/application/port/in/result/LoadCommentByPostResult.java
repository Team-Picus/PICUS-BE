package com.picus.core.post.application.port.in.result;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadCommentByPostResult(
        String commentNo,
        String authorNo,
        String authorNickname,
        String authorProfileImageUrl,
        String content,
        LocalDateTime createdAt
) {
}
