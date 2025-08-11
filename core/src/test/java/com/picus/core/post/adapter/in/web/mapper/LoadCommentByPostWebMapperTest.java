package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadCommentByPostResponse;
import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class LoadCommentByPostWebMapperTest {

    private final LoadCommentByPostWebMapper webMapper = new LoadCommentByPostWebMapper();

    @Test
    @DisplayName("List<LoadCommentByPostResult> -> LoadCommentByPostResponse 매핑")
    void toResponse_mapsResultListToResponse() {
        // given
        LoadCommentByPostResult result1 = LoadCommentByPostResult.builder()
                .commentNo("c1")
                .authorNo("u1")
                .authorNickname("nick1")
                .authorProfileImageUrl("url1")
                .content("content1")
                .createdAt(LocalDateTime.of(2025, 8, 6, 12, 0))
                .build();
        LoadCommentByPostResult result2 = LoadCommentByPostResult.builder()
                .commentNo("c2")
                .authorNo("u2")
                .authorNickname("nick2")
                .authorProfileImageUrl("url2")
                .content("content2")
                .createdAt(LocalDateTime.of(2025, 8, 6, 13, 30))
                .build();

        // when
        LoadCommentByPostResponse response = webMapper.toResponse(List.of(result1, result2));

        // then
        assertThat(response.comments())
            .extracting(
                    LoadCommentByPostResponse.CommentResponse::commentNo,
                    LoadCommentByPostResponse.CommentResponse::authorNo,
                    LoadCommentByPostResponse.CommentResponse::authorNickname,
                    LoadCommentByPostResponse.CommentResponse::authorProfileImageUrl,
                    LoadCommentByPostResponse.CommentResponse::content,
                    LoadCommentByPostResponse.CommentResponse::createdAt
            )
            .containsExactlyInAnyOrder(
                tuple("c1", "u1", "nick1", "url1", "content1", LocalDateTime.of(2025, 8, 6, 12, 0)),
                tuple("c2", "u2", "nick2", "url2", "content2", LocalDateTime.of(2025, 8, 6, 13, 30))
            );
    }
}