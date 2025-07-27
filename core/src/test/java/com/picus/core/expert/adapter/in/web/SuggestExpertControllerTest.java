package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.adapter.in.SuggestExpertController;
import com.picus.core.expert.adapter.in.web.mapper.SuggestExpertWebMapper;
import com.picus.core.expert.application.port.in.SuggestExpertsUseCase;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResp;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SuggestExpertController.class)
@AutoConfigureMockMvc(addFilters = false)
class SuggestExpertControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SuggestExpertsUseCase suggestExpertsUseCase;

    @MockitoBean
    private SuggestExpertWebMapper suggestExpertWebMapper;

    @Test
    @DisplayName("닉네임에 특정 keyword가 포함되는 전문가를 n명 추천해준다.")
    void suggestExperts_success() throws Exception {
        // given
        String keyword = "nick";
        int size = 2;
        List<SuggestExpertAppResp> mockResult = List.of(
                new SuggestExpertAppResp("ex1", "nick1", "aaa"),
                new SuggestExpertAppResp("ex2", "nick2", "bbb")
        );

        stubMethodInController(keyword, mockResult, size);

        // when & then
        mockMvc.perform(get("/api/v1/experts/search/suggestions")
                        .param("keyword", keyword)
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].expertNo").exists())
                .andExpect(jsonPath("$.result[0].nickname").exists())
                .andExpect(jsonPath("$.result[0].profileImageUrl").exists())
                .andExpect(jsonPath("$.result[1].expertNo").exists())
                .andExpect(jsonPath("$.result[1].nickname").exists())
                .andExpect(jsonPath("$.result[1].profileImageUrl").exists());

        then(suggestExpertsUseCase).should().suggestExperts(keyword, size);
    }

    @Test
    @DisplayName("size를 생략하면 3이 기본값으로 들어간다.")
    void suggestExperts_default_size_is_3() throws Exception {
        // given
        String keyword = "nick";

        // when & then
        mockMvc.perform(get("/api/v1/experts/search/suggestions")
                        .param("keyword", keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").isArray());

        then(suggestExpertsUseCase).should().suggestExperts(keyword, 3);
    }

    @Test
    @DisplayName("전문가 검색어 추천을 받을 때 키워드를 누락하면 400 에러가 발생한다.")
    void searchExperts_missingKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/experts/search/suggestions"))
                .andExpect(status().isBadRequest());
    }

    private void stubMethodInController(String keyword, List<SuggestExpertAppResp> mockResult, int size) {
        given(suggestExpertsUseCase.suggestExperts(keyword, size)).willReturn(mockResult);
        for (SuggestExpertAppResp app : mockResult) {
            given(suggestExpertWebMapper.toWebResponse(app)).willReturn(
                    new com.picus.core.expert.adapter.in.web.data.response.SuggestExpertWebResponse(
                            app.expertNo(), app.nickname(), app.profileImageUrl()
                    )
            );
        }
    }
}