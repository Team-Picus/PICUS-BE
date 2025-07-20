package com.picus.core.expert.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.SkillWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.StudioWebRequest;
import com.picus.core.expert.adapter.in.web.mapper.UpdateExpertWebMapper;
import com.picus.core.expert.application.port.in.ExpertInfoCommand;
import com.picus.core.expert.application.port.in.command.UpdateExpertBasicInfoAppRequest;
import com.picus.core.expert.application.port.in.command.UpdateExpertDetailInfoAppRequest;
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

import java.util.List;

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
        UpdateExpertBasicInfoWebRequest request = givenWebRequest();
        String currentUserNo = TEST_USER_ID;

        given(updateExpertWebMapper.toBasicInfoAppRequest(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertBasicInfoAppRequest.class));

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
                .updateExpertBasicInfo(any(UpdateExpertBasicInfoAppRequest.class));
    }

    @Test
    @DisplayName("전문가의 상세정보를 수정한다.")
    public void updateExpertDetailInfo_success() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoWebRequest request = UpdateExpertDetailInfoWebRequest.builder()
                .activityCareer("촬영 5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(ProjectWebRequest.builder().projectName("프로젝트1").build()))
                .skills(List.of(SkillWebRequest.builder().skillType("CAMERA").content("소니 카메라 운용").build()))
                .studio(StudioWebRequest.builder().studioName("필름 스튜디오").employeesCount(5).build())
                .build();

        given(updateExpertWebMapper.toDetailInfoAppRequest(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoAppRequest.class));

        // when
        mockMvc.perform(
                        patch("/api/v1/experts/detail_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));

        // then
        then(updateExpertWebMapper).should()
                .toDetailInfoAppRequest(request, currentUserNo);
        then(expertInfoCommand).should()
                .updateExpertDetailInfo(any(UpdateExpertDetailInfoAppRequest.class));
    }

    @Test
    @DisplayName("전문가의 상세정보를 수정할 때, activityAreas가 누락되면 오류가 발생한다.")
    public void updateExpertDetailInfo_activityAreas_null() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoWebRequest request = UpdateExpertDetailInfoWebRequest.builder()
                .activityCareer("촬영 5년 경력")
                .projects(List.of(ProjectWebRequest.builder().projectName("프로젝트1").build()))
                .skills(List.of(SkillWebRequest.builder().skillType("CAMERA").content("소니 카메라 운용").build()))
                .studio(StudioWebRequest.builder().studioName("필름 스튜디오").employeesCount(5).build())
                .build();

        given(updateExpertWebMapper.toDetailInfoAppRequest(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoAppRequest.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/detail_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    private UpdateExpertBasicInfoWebRequest givenWebRequest() {
        return UpdateExpertBasicInfoWebRequest.builder()
                .backgroundImageFileKey("new-background")
                .link("https://new.link")
                .intro("New intro")
                .nickname("NewNickname")
                .profileImageFileKey("new-profile-img")
                .build();
    }

}