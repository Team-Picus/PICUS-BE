package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.adapter.in.web.data.request.SearchPostRequest;
import com.picus.core.post.adapter.in.web.data.response.SearchPostResponse;
import com.picus.core.post.adapter.in.web.mapper.SearchPostWebMapper;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchPostWebMapperTest {

    private final SearchPostWebMapper mapper = new SearchPostWebMapper();

    @Test
    void toCommand_shouldMapRequestToCommand() {
        SearchPostRequest request = new SearchPostRequest(
                List.of(PostThemeType.SNAP),
                List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR,
                "서울 강남구",
                List.of(PostMoodType.COZY),
                "updatedAt",
                "DESC",
                "post-123",
                20
        );

        SearchPostCommand command = mapper.toCommand(request);

        assertThat(command.themeTypes()).containsExactly(PostThemeType.SNAP);
        assertThat(command.snapSubThemes()).containsExactly(SnapSubTheme.FAMILY);
        assertThat(command.spaceType()).isEqualTo(SpaceType.OUTDOOR);
        assertThat(command.address()).isEqualTo("서울 강남구");
        assertThat(command.moodTypes()).containsExactly(PostMoodType.COZY);
        assertThat(command.sortBy()).isEqualTo("updatedAt");
        assertThat(command.sortDirection()).isEqualTo("DESC");
        assertThat(command.lastPostNo()).isEqualTo("post-123");
        assertThat(command.size()).isEqualTo(20);
    }

    @Test
    void toResponse_shouldMapResultToResponse() {
        SearchPostResult.PostResult postResult = SearchPostResult.PostResult
                .builder()
                .postNo("post-123")
                .thumbnailUrl("http://img.url")
                .authorNickname("John")
                .title("Post")
                .build();

        SearchPostResult result = SearchPostResult.builder()
                .posts(List.of(postResult))
                .isLast(true)
                .build();

        SearchPostResponse response = mapper.toResponse(result);

        assertThat(response.isLast()).isTrue();
        assertThat(response.posts()).hasSize(1);
        SearchPostResponse.PostResponse post = response.posts().getFirst();
        assertThat(post.postNo()).isEqualTo("post-123");
        assertThat(post.thumbnailUrl()).isEqualTo("http://img.url");
        assertThat(post.authorNickname()).isEqualTo("John");
        assertThat(post.title()).isEqualTo("Post");
    }
}