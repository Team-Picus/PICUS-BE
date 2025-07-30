package com.picus.core.post.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.response.SuggestPostsResponse;
import com.picus.core.post.adapter.in.web.mapper.SuggestPostsWebMapper;
import com.picus.core.post.application.port.in.SuggestPostsUseCase;
import com.picus.core.post.application.port.in.result.SuggestPostsResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SuggestPostsController.class)
@AutoConfigureMockMvc(addFilters = false)
class SuggestPostsControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    SuggestPostsUseCase suggestPostsUseCase;
    @MockitoBean
    SuggestPostsWebMapper webMapper;

    @Test
    @DisplayName("게시물 검색어 추천 요청을 한다.")
    public void suggest_success() throws Exception {
        // given
        String keyword = "k";
        int size = 5;

        SuggestPostsResult suggestPostsResult = mock(SuggestPostsResult.class);
        given(suggestPostsUseCase.suggest(keyword, size)).willReturn(List.of(suggestPostsResult));
        SuggestPostsResponse suggestPostsResponse = SuggestPostsResponse.builder()
                .postId("post-123")
                .title("title")
                .build();
        given(webMapper.toResponse(suggestPostsResult)).willReturn(suggestPostsResponse);

        // when
        mockMvc.perform(get("/api/v1/posts/search/suggestions")
                        .param("keyword", keyword)
                        .param("size", String.valueOf(size))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].postId").exists())
                .andExpect(jsonPath("$.result[0].title").exists());

        // then
        then(suggestPostsUseCase).should().suggest(keyword, size);
        then(webMapper).should().toResponse(suggestPostsResult);
    }

    @Test
    @DisplayName("게시물 검색어 추천 요청을 할 때 keyword가 누락되면 에러가 발생한다.")
    public void suggest_ifKeywordNull() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/v1/posts/search/suggestions")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
        // then
    }

    @Test
    @DisplayName("게시물 검색어 추천 요청을 할 때, size를 누락하면 5로 설정된다.")
    public void suggest_success_ifSizeisNull() throws Exception {
        // given
        String keyword = "k";

        // when
        mockMvc.perform(get("/api/v1/posts/search/suggestions")
                        .param("keyword", keyword)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        // then
        then(suggestPostsUseCase).should().suggest(keyword, 5);
    }
}