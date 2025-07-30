package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.mapper.SuggestPostsCommandMapper;
import com.picus.core.post.application.port.in.result.SuggestPostsResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SuggestPostsServiceTest {

    @Mock private PostReadPort postReadPort;
    @Mock private SuggestPostsCommandMapper appMapper;

    @InjectMocks SuggestPostsService suggestPostsService;

    @Test
    @DisplayName("게시물 검색어 추천")
    public void suggest_success() throws Exception {
        // given
        String keyword = "k";
        int size = 5;

        Post post = mock(Post.class);
        given(postReadPort.findTopNByTitleContainingOrderByTitle(keyword, size))
                .willReturn(List.of(post));

        SuggestPostsResult appResp = mock(SuggestPostsResult.class);
       given(appMapper.toAppResp(post)).willReturn(appResp);

        // when
        List<SuggestPostsResult> results = suggestPostsService.suggest(keyword, size);

        // then
        assertThat(results).hasSize(1);
        then(postReadPort).should().findTopNByTitleContainingOrderByTitle(keyword, size);
        then(appMapper).should().toAppResp(post);
    }


}