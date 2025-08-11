package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadCommentByPostResponse;
import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadCommentByPostWebMapper {

    public LoadCommentByPostResponse toResponse(List<LoadCommentByPostResult> results) {

        return LoadCommentByPostResponse.builder()
                .comments(
                        results.stream()
                                .map(this::toCommentResponse)
                                .toList()
                )
                .build();
    }

    private LoadCommentByPostResponse.CommentResponse toCommentResponse(LoadCommentByPostResult result) {
        return LoadCommentByPostResponse.CommentResponse.builder()
                .commentNo(result.commentNo())
                .authorNo(result.authorNo())
                .authorNickname(result.authorNickname())
                .authorProfileImageUrl(result.authorProfileImageUrl())
                .content(result.content())
                .createdAt(result.createdAt())
                .build();
    }
}
