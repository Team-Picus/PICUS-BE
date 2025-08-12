package com.picus.core.expert.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.SkillWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.StudioWebRequest;
import com.picus.core.expert.adapter.in.web.mapper.UpdateExpertWebMapper;
import com.picus.core.expert.application.port.in.UpdateExpertUseCase;
import com.picus.core.expert.application.port.in.command.ChangeStatus;
import com.picus.core.expert.application.port.in.command.UpdateExpertBasicInfoCommand;
import com.picus.core.expert.application.port.in.command.UpdateExpertDetailInfoCommand;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.vo.SkillType;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(controllers = UpdateExpertController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UpdateExpertControllerTest extends AbstractSecurityMockSetup {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    UpdateExpertUseCase updateExpertUseCase;
    @MockitoBean
    UpdateExpertWebMapper updateExpertWebMapper;

    @Test
    @DisplayName("전문가의 기본정보를 수정한다.")
    public void updateExpertBasicInfo_success() throws Exception {
        // given
        UpdateExpertBasicInfoRequest request = givenWebRequest();
        String currentUserNo = TEST_USER_ID;

        given(updateExpertWebMapper.toBasicInfoCommand(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertBasicInfoCommand.class));

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
                .toBasicInfoCommand(eq(request), eq(TEST_USER_ID));
        then(updateExpertUseCase).should()
                .updateExpertBasicInfo(any(UpdateExpertBasicInfoCommand.class));
    }

    @Test
    @DisplayName("전문가의 상세정보를 수정한다.")
    public void updateExpertDetailInfo_success() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoRequest request = UpdateExpertDetailInfoRequest.builder()
                .activityCareer("촬영 5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(ProjectWebRequest.builder()
                        .projectName("프로젝트1")
                        .startDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                        .endDate(LocalDateTime.of(2000, 2, 2, 2, 2))
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .skills(List.of(SkillWebRequest.builder()
                        .skillType(SkillType.CAMERA)
                        .content("소니 카메라 운용")
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .studio(StudioWebRequest.builder()
                        .studioName("필름 스튜디오")
                        .employeesCount(5)
                        .changeStatus(ChangeStatus.NEW)
                        .businessHours("20:00~24:00")
                        .address("서울 강남구")
                        .build())
                .build();

        given(updateExpertWebMapper.toDetailInfoCommand(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoCommand.class));

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
                .toDetailInfoCommand(request, currentUserNo);
        then(updateExpertUseCase).should()
                .updateExpertDetailInfo(any(UpdateExpertDetailInfoCommand.class));
    }

    @Test
    @DisplayName("전문가의 상세정보를 수정할 때, activityCareer가 blank 오류가 발생한다.")
    public void updateExpertDetailInfo_activityCareer_blank() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoRequest request = UpdateExpertDetailInfoRequest.builder()
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(ProjectWebRequest.builder()
                        .projectName("프로젝트1")
                        .startDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                        .endDate(LocalDateTime.of(2000, 2, 2, 2, 2))
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .skills(List.of(SkillWebRequest.builder()
                        .skillType(SkillType.CAMERA)
                        .content("소니 카메라 운용")
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .studio(StudioWebRequest.builder()
                        .studioName("필름 스튜디오")
                        .employeesCount(5)
                        .changeStatus(ChangeStatus.NEW)
                        .businessHours("20:00~24:00")
                        .address("서울 강남구")
                        .build())
                .build();

        given(updateExpertWebMapper.toDetailInfoCommand(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoCommand.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/detail_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("전문가의 상세정보를 수정할 때, activityAreas가 누락되면 오류가 발생한다.")
    public void updateExpertDetailInfo_activityAreas_null() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoRequest request = UpdateExpertDetailInfoRequest.builder()
                .activityCareer("촬영 5년 경력")
                .projects(List.of(ProjectWebRequest.builder()
                        .projectName("프로젝트1")
                        .startDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                        .endDate(LocalDateTime.of(2000, 2, 2, 2, 2))
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .skills(List.of(SkillWebRequest.builder()
                        .skillType(SkillType.CAMERA)
                        .content("소니 카메라 운용")
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .studio(StudioWebRequest.builder()
                        .studioName("필름 스튜디오")
                        .employeesCount(5)
                        .changeStatus(ChangeStatus.NEW)
                        .businessHours("20:00~24:00")
                        .address("서울 강남구")
                        .build())
                .build();

        given(updateExpertWebMapper.toDetailInfoCommand(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoCommand.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/detail_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("전문가의 상세정보를 수정시 Project/Skill/Studio를 수정하려고 할 때 id를 제외한 특정 필드가 누락되면 400 에러가 발생한다.")
    public void updateExpertDetailInfo_Project_Skill_Studio_filed_null() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoRequest request = UpdateExpertDetailInfoRequest.builder()
                .activityCareer("촬영 5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(ProjectWebRequest.builder()
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .skills(List.of(SkillWebRequest.builder()
                        .changeStatus(ChangeStatus.NEW)
                        .build()))
                .studio(StudioWebRequest.builder()
                        .changeStatus(ChangeStatus.NEW)
                        .build())
                .build();

        given(updateExpertWebMapper.toDetailInfoCommand(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoCommand.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/detail_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



    @Test
    @DisplayName("전문가의 상세정보를 수정시 Project/Skill/Studio를 수정하려고 할 때 change_status가 누락되면 400 에러가 발생한다.")
    public void updateExpertDetailInfo_change_status_null() throws Exception {
        // given
        String currentUserNo = TEST_USER_ID;
        UpdateExpertDetailInfoRequest request = UpdateExpertDetailInfoRequest.builder()
                .activityCareer("촬영 5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(ProjectWebRequest.builder()
                        .projectName("프로젝트1")
                        .startDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                        .endDate(LocalDateTime.of(2000, 2, 2, 2, 2))
                        .build()))
                .skills(List.of(SkillWebRequest.builder()
                        .skillType(SkillType.CAMERA)
                        .content("소니 카메라 운용")
                        .build()))
                .studio(StudioWebRequest.builder()
                        .studioName("필름 스튜디오")
                        .employeesCount(5)
                        .businessHours("20:00~24:00")
                        .address("서울 강남구")
                        .build())
                .build();

        given(updateExpertWebMapper.toDetailInfoCommand(request, currentUserNo))
                .willReturn(Mockito.mock(UpdateExpertDetailInfoCommand.class));

        // when // then
        mockMvc.perform(
                        patch("/api/v1/experts/detail_info")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    private UpdateExpertBasicInfoRequest givenWebRequest() {
        return UpdateExpertBasicInfoRequest.builder()
                .backgroundImageFileKey("new-background")
                .link(List.of("https://new.link"))
                .intro("New intro")
                .nickname("NewNickname")
                .profileImageFileKey("new-profile-img")
                .build();
    }

}