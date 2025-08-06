package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadCommentByPostResponse(
         List<CommentResponse> comments
) {
    @Builder
    public record CommentResponse(
            String commentNo,
            String authorNo,
            String authorNickname,
            String authorProfileImageUrl,
            String content,
            LocalDateTime createdAt
    ){}
}
