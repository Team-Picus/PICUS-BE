package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.SearchExpertWebMapper;
import com.picus.core.expert.application.port.in.SearchExpertsQuery;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
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
class SearchExpertControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchExpertsQuery searchExpertsQuery;

    @MockitoBean
    private SearchExpertWebMapper searchExpertWebMapper;

    @Test
    @DisplayName("닉네임에 특정 keyword가 포함되는 전문가를 검색하면 필드가 존재한다")
    void searchExperts_success() throws Exception {
        // given
        String keyword = "nick";
        List<SearchExpertAppResponse> mockAppResponses = List.of(
                new SearchExpertAppResponse("ex1", "nick1", "url1"),
                new SearchExpertAppResponse("ex2", "nick2", "url2")
        );
        List<SearchExpertWebResponse> mockWebResponses = List.of(
                SearchExpertWebResponse.builder()
                        .expertNo("ex1")
                        .nickname("nick1")
                        .profileImageUrl("url1")
                        .build(),
                SearchExpertWebResponse.builder()
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

        then(searchExpertsQuery).should().searchExperts(keyword);
        for (SearchExpertAppResponse appResponse : mockAppResponses) {
            then(searchExpertWebMapper).should().toWebResponse(appResponse);
        }
    }

    @Test
    @DisplayName("전문가 검색 시 키워드를 누락하면 400 에러가 발생한다.")
    void searchExperts_missingKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/experts/search/results"))
                .andExpect(status().isBadRequest());
    }

    private void stubMethodInController(String keyword,
                                        List<SearchExpertAppResponse> appResponses,
                                        List<SearchExpertWebResponse> webResponses) {
        given(searchExpertsQuery.searchExperts(keyword)).willReturn(appResponses);

        for (int i = 0; i < appResponses.size(); i++) {
            given(searchExpertWebMapper.toWebResponse(appResponses.get(i))).willReturn(webResponses.get(i));
        }
    }
}