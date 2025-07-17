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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchExpertController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchExpertControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchExpertsQuery searchExpertsQuery;
    @MockitoBean
    private SearchExpertWebMapper searchExpertWebMapper;

    @Test
    @DisplayName("닉네임에 특정 keyword가 포함되는 전문가를 검색한다")
    void searchExperts_success() throws Exception {
        // given
        String keyword = "nick";
        List<SearchExpertAppResponse> mockResult = List.of(
                new SearchExpertAppResponse("ex1", "nick1", "aaa"),
                new SearchExpertAppResponse("ex2", "nick2", "bbb")
        );

        stubMethodInController(keyword, mockResult);


        // when & then
        mockMvc.perform(get("/api/v1/experts/search/results")
                        .param("keyword", keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());

        then(searchExpertsQuery).should().searchExperts(keyword);
        then(searchExpertWebMapper).should(times(2))
                .toWebResponse(any(SearchExpertAppResponse.class));
    }

    @Test
    @DisplayName("전문가 검색 시 키워드를 누락하면 400 에러가 발생한다.")
    void searchExperts_missingKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/experts/search/results"))
                .andExpect(status().isBadRequest());
    }

    private void stubMethodInController(String keyword, List<SearchExpertAppResponse> mockResult) {
        given(searchExpertsQuery.searchExperts(keyword))
                .willReturn(mockResult);
        mockResult.forEach(searchExpertAppResponse ->
                given(searchExpertWebMapper.toWebResponse(searchExpertAppResponse))
                        .willReturn(new SearchExpertWebResponse(
                                searchExpertAppResponse.expertNo(),
                                searchExpertAppResponse.nickname(),
                                searchExpertAppResponse.profileImageUrl())));
    }

}