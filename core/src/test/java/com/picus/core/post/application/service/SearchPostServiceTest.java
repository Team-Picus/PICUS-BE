package com.picus.core.post.application.service;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.application.port.in.mapper.SearchPostCommandMapper;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SearchPostServiceTest {

    @Mock
    private PostReadPort postReadPort;
    @Mock
    private UserReadPort userReadPort;
    @Mock
    private SearchPostCommandMapper commandMapper;

    @InjectMocks
    private SearchPostService searchPostService;

    @Test
    @DisplayName("게시물을 검색한다.")
    public void search() throws Exception {
        // given
        SearchPostCommand command = new SearchPostCommand(List.of(PostThemeType.SNAP), List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR, "서울 강남구", List.of(PostMoodType.COZY), "updatedAt",
                "DESC", "post-123", 20);

        SearchPostCond mockCond = mock(SearchPostCond.class);
        given(commandMapper.toCond(command)).willReturn(mockCond);

        Post mockPost = Post.builder()
                .postNo("post-123")
                .authorNo("expert-123")
                .title("title")
                .build();
        given(postReadPort.findBySearchCond(
                mockCond, command.cursor(), command.sortBy(), command.sortDirection(), command.size() + 1))
                .willReturn(List.of(mockPost));

        User mockUser = User.builder().nickname("user").build();
        given(userReadPort.findByExpertNo(mockPost.getAuthorNo())).willReturn(mockUser);


        // when 
        SearchPostResult result = searchPostService.search(command);

        // then - 메서드 호출 검증
        then(commandMapper).should().toCond(command);
        then(postReadPort).should().findBySearchCond(
                mockCond, command.cursor(), command.sortBy(), command.sortDirection(), command.size() + 1);
        then(userReadPort).should().findByExpertNo(mockPost.getAuthorNo());

        // then - 리턴값 검증
        assertThat(result.posts()).hasSize(1)
                .extracting(
                        SearchPostResult.PostResult::postNo,
                        SearchPostResult.PostResult::authorNickname,
                        SearchPostResult.PostResult::thumbnailUrl,
                        SearchPostResult.PostResult::title
                ).containsExactlyInAnyOrder(
                        // TODO: file key -> url 변환 로직
                        tuple(mockPost.getPostNo(), mockUser.getNickname(), "", mockPost.getTitle())
                );
        assertThat(result.isLast()).isTrue();
    }
}