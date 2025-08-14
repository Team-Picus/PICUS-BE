package com.picus.core.expert.adapter.in;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertResponse;
import com.picus.core.expert.adapter.in.web.mapper.SearchExpertWebMapper;
import com.picus.core.expert.application.port.in.SearchExpertsUseCase;
import com.picus.core.expert.application.port.in.result.SearchExpertResult;
import com.picus.core.shared.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchExpertController.class)
@Import(SearchExpertWebMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchExpertControllerTest extends ControllerTestSupport {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchExpertsUseCase searchExpertsUseCase;

    @MockitoBean
    private SearchExpertWebMapper searchExpertWebMapper;

    @Test
    @DisplayName("닉네임에 특정 keyword가 포함되는 전문가를 검색하면 필드가 존재한다")
    void searchExperts_success() throws Exception {
        // given
        String keyword = "nick";
        List<SearchExpertResult> mockAppResponses = List.of(
                new SearchExpertResult("ex1", "nick1", "url1"),
                new SearchExpertResult("ex2", "nick2", "url2")
        );
        List<SearchExpertResponse> mockWebResponses = List.of(
                SearchExpertResponse.builder()
                        .expertNo("ex1")
                        .nickname("nick1")
                        .profileImageUrl("url1")
                        .build(),
                SearchExpertResponse.builder()
                        .expertNo("ex2")
                        .nickname("nick2")
                        .profileImageUrl("url2")
                        .build()
        );

        stubMethodInController(keyword, mockAppResponses, mockWebResponses);

        // when & then
        mockMvc.perform(get("/api/v1/experts/search/results")
                        .param("keyword", keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].expertNo").exists())
                .andExpect(jsonPath("$.result[0].nickname").exists())
                .andExpect(jsonPath("$.result[0].profileImageUrl").exists());

        then(searchExpertsUseCase).should().searchExperts(keyword);
        for (SearchExpertResult appResponse : mockAppResponses) {
            then(searchExpertWebMapper).should().toResponse(appResponse);
        }
    }

    @Test
    @DisplayName("전문가 검색 시 키워드를 누락하면 400 에러가 발생한다.")
    void searchExperts_missingKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/experts/search/results"))
                .andExpect(status().isBadRequest());
    }

    private void stubMethodInController(String keyword,
                                        List<SearchExpertResult> appResponses,
                                        List<SearchExpertResponse> webResponses) {
        given(searchExpertsUseCase.searchExperts(keyword)).willReturn(appResponses);

        for (int i = 0; i < appResponses.size(); i++) {
            given(searchExpertWebMapper.toResponse(appResponses.get(i))).willReturn(webResponses.get(i));
        }
    }
}