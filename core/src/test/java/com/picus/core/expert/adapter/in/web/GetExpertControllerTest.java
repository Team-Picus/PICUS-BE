package com.picus.core.expert.adapter.in.web;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse.StudioWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.GetExpertWebMapper;
import com.picus.core.expert.application.port.in.ExpertInfoQuery;
import com.picus.core.expert.application.port.in.response.ExpertBasicInfoQueryAppResp;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = GetExpertController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class GetExpertControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpertInfoQuery expertInfoQuery;
    @MockitoBean
    private GetExpertWebMapper getExpertWebMapper;

    @Test
    @DisplayName("특정 전문가의 기본정보를 조회한다.")
    public void getExpertBasicInfo_success() throws Exception {
        // given
        String expertNo = "expert_no1";
        stubMethodAboutBasicInfo(expertNo);

        // when then - 응답 검증
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/basic_info",
                                expertNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.expertNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.activityDuration").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.activityCount").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.lastActivityAt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.intro").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.backgroundImageUrl").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.nickname").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.profileImageUrl").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.links").exists());


        // then - 메서드 호출 검증
        then(expertInfoQuery).should()
                .getExpertBasicInfo(expertNo);
        then(getExpertWebMapper).should()
                .toBasicInfo(any(ExpertBasicInfoQueryAppResp.class));
    }

    @Test
    @DisplayName("특정 전문가의 상세정보를 조회한다.")
    public void getExpertDetailInfo_success() throws Exception {
        // given
        String expertNo = "expert_no1";
        stubMethodAboutDetailInfo(expertNo);

        // when then - 응답 검증
        mockMvc.perform(
                        get("/api/v1/experts/{expert_no}/detail_info",
                                expertNo)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.activityCareer").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.projects").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.skills").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.activityAreas").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.studio").exists());

        // then - 메서드 호출 검증
        then(expertInfoQuery).should()
                .getExpertDetailInfo(expertNo);
        then(getExpertWebMapper).should()
                .toDetailInfo(any(Expert.class));
    }

    private void stubMethodAboutBasicInfo(String expertNo) {
        ExpertBasicInfoQueryAppResp mockAppResponse = ExpertBasicInfoQueryAppResp.builder()
                .expertNo("")
                .activityDuration("")
                .activityCount(100)
                .lastActivityAt(LocalDateTime.now())
                .intro("")
                .backgroundImageUrl("")
                .nickname("")
                .profileImageUrl("")
                .build();
        // Mock이면 값들이 Null로 채워지는데, 그러면 exists()검증이 안됨
        GetExpertBasicInfoWebResponse mockWebResponse = GetExpertBasicInfoWebResponse.builder()
                .expertNo("")
                .activityDuration("")
                .activityCount(100)
                .lastActivityAt(LocalDateTime.now())
                .intro("")
                .backgroundImageUrl("")
                .nickname("")
                .profileImageUrl("")
                .links(List.of())
                .build();

        given(expertInfoQuery.getExpertBasicInfo(expertNo))
                .willReturn(mockAppResponse);
        given(getExpertWebMapper.toBasicInfo(mockAppResponse))
                .willReturn(mockWebResponse);
    }

    private void stubMethodAboutDetailInfo(String expertNo) {
        Expert mockExpert = mock(Expert.class);
        // Mock이면 값들이 Null로 채워지는데, 그러면 exists()검증이 안됨
        GetExpertDetailInfoWebResponse webResponse =
                GetExpertDetailInfoWebResponse.builder()
                        .activityCareer("")
                        .projects(List.of())
                        .skills(List.of())
                        .activityAreas(List.of())
                        .studio(StudioWebResponse.builder().build())
                        .build();

        given(expertInfoQuery.getExpertDetailInfo(expertNo))
                .willReturn(mockExpert);
        given(getExpertWebMapper.toDetailInfo(mockExpert))
                .willReturn(webResponse);
    }


}