package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.mapper.SuggestPostsAppMapper;
import com.picus.core.post.application.port.in.response.SuggestPostsAppResp;
import com.picus.core.post.application.port.out.ReadPostPort;
import com.picus.core.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SuggestPostsServiceTest {

    @Mock private ReadPostPort readPostPort;
    @Mock private SuggestPostsAppMapper appMapper;

    @InjectMocks SuggestPostsService suggestPostsService;

    @Test
    @DisplayName("게시물 검색어 추천")
    public void suggest_success() throws Exception {
        // given
        String keyword = "k";
        int size = 5;

        Post post = mock(Post.class);
        given(readPostPort.findTopNByTitleContainingOrderByTitle(keyword, size))
                .willReturn(List.of(post));

        SuggestPostsAppResp appResp = mock(SuggestPostsAppResp.class);
       given(appMapper.toAppResp(post)).willReturn(appResp);

        // when
        List<SuggestPostsAppResp> results = suggestPostsService.suggest(keyword, size);

        // then
        assertThat(results).hasSize(1);
        then(readPostPort).should().findTopNByTitleContainingOrderByTitle(keyword, size);
        then(appMapper).should().toAppResp(post);
    }


}