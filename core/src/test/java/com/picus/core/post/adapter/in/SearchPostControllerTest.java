package com.picus.core.post.adapter.in;

import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import com.picus.core.post.adapter.in.web.data.request.SearchPostRequest;
import com.picus.core.post.adapter.in.web.data.response.SearchPostResponse;
import com.picus.core.post.application.port.in.SearchPostUseCase;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.adapter.in.web.mapper.SearchPostWebMapper;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchPostController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchPostControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    SearchPostUseCase searchPostUseCase;
    @MockitoBean
    SearchPostWebMapper webMapper;


    @Test
    @DisplayName("게시물 검색 요청한다.")
    void search_success() throws Exception {
        // given:
        SearchPostRequest request = new SearchPostRequest(
                List.of(PostThemeType.SNAP),
                List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR,
                "서울 강남구",
                List.of(PostMoodType.COZY),
                "createdAt",
                "DESC",
                "post-123",
                10
        );

        stub(request);

        // when & then
        mockMvc.perform(
                        get("/api/v1/posts/search/results")
                                .params(convertToParams(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("기본 생성자에서 누락된 값은 기본값으로 채워져야 한다.")
    void search_withDefaultValues() throws Exception {
        // given: 일부 값만 명시하고 나머지는 null
        SearchPostRequest request = new SearchPostRequest(
                List.of(PostThemeType.SNAP),
                List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR,
                "서울 강남구",
                List.of(PostMoodType.COZY),
                null,      // sortBy = "createdAt"
                null,      // sortDirection = "DESC"
                "post-123",
                null       // size = 10
        );

        stub(request);

        // when & then
        mockMvc.perform(
                        get("/api/v1/posts/search/results")
                                .params(convertToParams(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 sortBy 값이 들어오면 오류가 발생한다.")
    void search_withWrongSortBy() throws Exception {
        // given
        SearchPostRequest request = new SearchPostRequest(
                List.of(PostThemeType.SNAP),
                List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR,
                "서울 강남구",
                List.of(PostMoodType.COZY),
                "wrongValue",
                null,
                "post-123",
                null
        );

        stub(request);

        // when & then
        mockMvc.perform(
                        get("/api/v1/posts/search/results")
                                .params(convertToParams(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 sortDirection 값이 들어오면 오류가 발생한다.")
    void search_withWrongSortDirection() throws Exception {
        // given
        SearchPostRequest request = new SearchPostRequest(
                List.of(PostThemeType.SNAP),
                List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR,
                "서울 강남구",
                List.of(PostMoodType.COZY),
                "createdAt",
                "wrongValue",
                "post-123",
                null
        );

        stub(request);

        // when & then
        mockMvc.perform(
                        get("/api/v1/posts/search/results")
                                .params(convertToParams(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    private void stub(SearchPostRequest request) {
        SearchPostCommand mockCommand = mock(SearchPostCommand.class);
        given(webMapper.toCommand(request)).willReturn(mockCommand);

        SearchPostResult mockResult = mock(SearchPostResult.class);
        given(searchPostUseCase.search(mockCommand)).willReturn(mockResult);

        SearchPostResponse.PostResponse postResponse = SearchPostResponse.PostResponse
                .builder()
                .postNo("post-123")
                .thumbnailUrl("http://img.url")
                .authorNickname("John")
                .title("Post")
                .build();

        SearchPostResponse response = SearchPostResponse.builder()
                .posts(List.of(postResponse))
                .isLast(true)
                .build();
        given(webMapper.toResponse(mockResult)).willReturn(response);
    }

    private MultiValueMap<String, String> convertToParams(SearchPostRequest request) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        request.getThemeTypes().forEach(type -> params.add("themeTypes", type.name()));
        request.getSnapSubThemes().forEach(sub -> params.add("snapSubThemes", sub.name()));
        if (request.getSpaceType() != null) params.add("spaceType", request.getSpaceType().name());
        if (request.getAddress() != null) params.add("address", request.getAddress());
        request.getMoodTypes().forEach(mood -> params.add("moodTypes", mood.name()));
        if (request.getSortBy() != null) params.add("sortBy", request.getSortBy());
        if (request.getSortDirection() != null) params.add("sortDirection", request.getSortDirection());
        if (request.getCursor() != null) params.add("cursor", request.getCursor());
        params.add("size", String.valueOf(request.getSize()));

        return params;
    }
}