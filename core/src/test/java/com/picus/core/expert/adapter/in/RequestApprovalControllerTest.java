package com.picus.core.expert.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.adapter.in.web.mapper.RequestApprovalWebMapper;
import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = RequestApprovalController.class)
@AutoConfigureMockMvc(addFilters = false)
class RequestApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RequestApprovalUseCase requestApprovalUseCase;
    @MockitoBean
    private RequestApprovalWebMapper webMapper;

    @Test
    @DisplayName("전문가 승인 요청을 한다.")
    public void requestApproval() throws Exception {
        // given
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest();

        // stubbing
        stubbingMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").isNotEmpty());


        // then - 메서드 호출 검증
        then(webMapper).should()
                .toDomain(eq(webRequest));
        then(requestApprovalUseCase).should()
                .requestApproval(any(Expert.class));
        then(webMapper).should()
                .toWebResponse(any(Expert.class));
    }

    private void stubbingMethodInController() {
        Expert mockExpert = Mockito.mock(Expert.class);
        RequestApprovalWebResponse mockResponse = Mockito.mock(RequestApprovalWebResponse.class);
        given(webMapper.toDomain(any(RequestApprovalWebRequest.class)))
                .willReturn(mockExpert);
        given(requestApprovalUseCase.requestApproval(any(Expert.class)))
                .willReturn(mockExpert);
        given(webMapper.toWebResponse(any(Expert.class)))
                .willReturn(mockResponse);
    }

    private RequestApprovalWebRequest givenRequestApprovalWebRequest() {
        return new RequestApprovalWebRequest(
                "3년차",
                List.of(ActivityArea.SEOUL_GANGBUKGU, ActivityArea.SEOUL_GANGDONGGU),
                List.of(
                        Project.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build(),
                        Project.builder()
                                .projectName("뮤직비디오 조명 작업")
                                .startDate(LocalDateTime.of(2023, 1, 10, 0, 0))
                                .endDate(LocalDateTime.of(2023, 2, 20, 0, 0))
                                .build()
                ),
                List.of(
                        Skill.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build(),
                        Skill.builder()
                                .skillType(SkillType.EDIT)
                                .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                                .build()
                ),
                Studio.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(),
                List.of(
                        Portfolio.builder()
                                .link("https://myportfolio.com/project1")
                                .build(),
                        Portfolio.builder()
                                .link("https://myportfolio.com/project2")
                                .build()
                )
        );
    }
}