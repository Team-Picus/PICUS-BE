package com.picus.core.expert.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.expert.adapter.in.web.data.request.ExpertBasicInfoCommandWebRequest;
import com.picus.core.expert.adapter.in.web.mapper.UpdateExpertWebMapper;
import com.picus.core.expert.application.port.in.ExpertInfoCommand;
import com.picus.core.expert.application.port.in.command.ExpertBasicInfoCommandRequest;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(controllers = UpdateExpertInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UpdateExpertInfoControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    ExpertInfoCommand expertInfoCommand;
    @MockitoBean
    UpdateExpertWebMapper updateExpertWebMapper;

    @Test
    @DisplayName("전문가의 기본정보를 수정한다.")
    public void updateExpertBasicInfo_success() throws Exception {
        // given
        ExpertBasicInfoCommandWebRequest request = givenWebRequest();
        String currentUserNo = TEST_USER_ID;

        given(updateExpertWebMapper.toBasicInfoAppRequest(request, currentUserNo))
                .willReturn(Mockito.mock(ExpertBasicInfoCommandRequest.class));

        // when
        mockMvc.perform(
                        patch("/api/v1/experts/basic_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        // then - 메서드 호출 검증
        then(updateExpertWebMapper).should()
                .toBasicInfoAppRequest(eq(request), eq(TEST_USER_ID));
        then(expertInfoCommand).should()
                .updateExpertBasicInfo(any(ExpertBasicInfoCommandRequest.class));
    }

    private ExpertBasicInfoCommandWebRequest givenWebRequest() {
        return ExpertBasicInfoCommandWebRequest.builder()
                .backgroundImageFileKey("new-background")
                .link("https://new.link")
                .intro("New intro")
                .nickname("NewNickname")
                .profileImageFileKey("new-profile-img")
                .build();
    }

}