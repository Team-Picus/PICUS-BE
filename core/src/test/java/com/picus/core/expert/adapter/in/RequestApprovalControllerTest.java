package com.picus.core.expert.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.in.web.mapper.RequestApprovalWebMapper;
import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.application.port.in.request.RequestApprovalCommand;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import jakarta.validation.constraints.NotBlank;
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
class RequestApprovalControllerTest extends AbstractSecurityMockSetup {

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
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest("3년차", List.of("서울 강북구", "서울 강동구"), List.of(
                RequestApprovalWebRequest.ProjectWebRequest.builder()
                        .projectName("단편영화 촬영 프로젝트")
                        .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                        .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                        .build(),
                RequestApprovalWebRequest.ProjectWebRequest.builder()
                        .projectName("뮤직비디오 조명 작업")
                        .startDate(LocalDateTime.of(2023, 1, 10, 0, 0))
                        .endDate(LocalDateTime.of(2023, 2, 20, 0, 0))
                        .build()
        ), List.of(
                RequestApprovalWebRequest.SkillWebRequest.builder()
                        .skillType(SkillType.CAMERA)
                        .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                        .build(),
                RequestApprovalWebRequest.SkillWebRequest.builder()
                        .skillType(SkillType.EDIT)
                        .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                        .build()
        ), RequestApprovalWebRequest.StudioWebRequest.builder()
                .studioName("크리에이티브 필름")
                .employeesCount(5)
                .businessHours("10:00 - 19:00")
                .address("서울특별시 마포구 월드컵북로 400")
                .build(), List.of(
                "https://myportfolio.com/project1",
                "https://myportfolio.com/project2"
        ));
        String currentUserId = TEST_USER_ID;

        // stubbing
        stubMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("요청에 성공하였습니다."));


        // then - 메서드 호출 검증
        then(webMapper).should()
                .toCommand(eq(webRequest), eq(TEST_USER_ID));
        then(requestApprovalUseCase).should()
                .requestApproval(any(RequestApprovalCommand.class));

    }

    @Test
    @DisplayName("전문가 승인 요청을 할 때 activity Career가 누락되면 오류발생")
    public void requestApproval_activityCareer_null() throws Exception {
        // given
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest(null, List.of("서울 강북구", "서울 강동구"),
                List.of(
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build()
                ), List.of(
                        RequestApprovalWebRequest.SkillWebRequest.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build()
                ), RequestApprovalWebRequest.StudioWebRequest.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(), List.of(
                        "https://myportfolio.com/project1"
                ));
        String currentUserId = TEST_USER_ID;

        // stubbing
        stubMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("전문가 승인 요청을 할 때 activity area가 누락되면 오류발생")
    public void requestApproval_activityAreas_null() throws Exception {
        // given
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest("3년차", null,
                List.of(
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build()
                ), List.of(
                        RequestApprovalWebRequest.SkillWebRequest.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build()
                ), RequestApprovalWebRequest.StudioWebRequest.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(), List.of(
                        "https://myportfolio.com/project1"
                ));
        String currentUserId = TEST_USER_ID;

        // stubbing
        stubMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("전문가 승인 요청을 할 때 activity areas의 필드가 블랭크면 오류발생")
    public void requestApproval_activityAreas_field_isBlank() throws Exception {
        // given
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest("3년차", List.of(" "),
                List.of(
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build()
                ), List.of(
                        RequestApprovalWebRequest.SkillWebRequest.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build()
                ), RequestApprovalWebRequest.StudioWebRequest.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(), List.of(
                        "https://myportfolio.com/project1"
                ));
        String currentUserId = TEST_USER_ID;

        // stubbing
        stubMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("전문가 승인 요청을 할 때 skill이 null이면 오류 발생")
    public void requestApproval_projects_isNull() throws Exception {
        // given
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest("3년차", List.of("서울 강북구", "서울 강동구"),
                List.of(
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build()
                ),
                null,
                RequestApprovalWebRequest.StudioWebRequest.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(), List.of(
                        "https://myportfolio.com/project1"
                ));
        String currentUserId = TEST_USER_ID;

        // stubbing
        stubMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("전문가 승인 요청을 할 때 skill의 특정 필드가 누락되면 오류 발생")
    public void requestApproval_projectsField_isNull() throws Exception {
        // given
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest("3년차", List.of("서울 강북구", "서울 강동구"),
                List.of(
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build()
                ),
                List.of(
                        RequestApprovalWebRequest.SkillWebRequest.builder()
                                .skillType(null)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build()
                ),
                RequestApprovalWebRequest.StudioWebRequest.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(), List.of(
                        "https://myportfolio.com/project1"
                ));
        String currentUserId = TEST_USER_ID;

        // stubbing
        stubMethodInController();

        // when then
        mockMvc.perform(
                        post("/api/v1/experts/approval-requests")
                                .content(objectMapper.writeValueAsString(webRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private void stubMethodInController() {
        RequestApprovalCommand mockCommand = Mockito.mock(RequestApprovalCommand.class);
        given(webMapper.toCommand(any(RequestApprovalWebRequest.class), eq(TEST_USER_ID)))
                .willReturn(mockCommand);
    }

    private RequestApprovalWebRequest givenRequestApprovalWebRequest(
            String activityCareer,
            List<@NotBlank String> activityAreas,
            List<RequestApprovalWebRequest.ProjectWebRequest> projectWebRequests,
            List<RequestApprovalWebRequest.SkillWebRequest> skillWebRequests,
            RequestApprovalWebRequest.StudioWebRequest studioWebRequest,
            List<String> portfolioLinks
    ) {
        return new RequestApprovalWebRequest(
                activityCareer,
                activityAreas,
                projectWebRequests,
                skillWebRequests,
                studioWebRequest,
                portfolioLinks
        );
    }
}