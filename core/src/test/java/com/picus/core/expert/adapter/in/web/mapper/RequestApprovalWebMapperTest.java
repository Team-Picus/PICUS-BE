package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalRequest;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalRequest.SkillWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalRequest.StudioWebRequest;
import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;
import com.picus.core.expert.domain.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestApprovalWebMapperTest {

    private final RequestApprovalWebMapper mapper = new RequestApprovalWebMapper();

    @Test
    @DisplayName("RequestApprovalWebRequest와 userNo를 RequestApprovalCommand로 변환한다")
    void toCommand_shouldMapCorrectly() {
        // given
        RequestApprovalRequest request = new RequestApprovalRequest(
                "백엔드 5년차",
                List.of("서울", "대구"),
                List.of(ProjectWebRequest.builder()
                        .projectName("쇼핑몰 개발")
                        .startDate(LocalDateTime.of(2022, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2022, 6, 30, 0, 0))
                        .build()),
                List.of(SkillWebRequest.builder()
                        .skillType(SkillType.EDIT)
                        .content("Spring Boot")
                        .build()),
                StudioWebRequest.builder()
                        .studioName("스튜디오명")
                        .employeesCount(5)
                        .businessHours("10:00 ~ 18:00")
                        .address("서울 강남구")
                        .build(),
                List.of("https://portfolio.site")
        );
        java.lang.String userNo = "user-001";

        // when
        RequestApprovalCommand command = mapper.toCommand(request, userNo);

        // then
        assertThat(command.userNo()).isEqualTo("user-001");
        assertThat(command.activityCareer()).isEqualTo("백엔드 5년차");
        assertThat(command.activityAreas()).containsExactly("서울", "대구");

        assertThat(command.projects()).hasSize(1);
        assertThat(command.projects().getFirst().getProjectName()).isEqualTo("쇼핑몰 개발");

        assertThat(command.skills()).hasSize(1);
        assertThat(command.skills().getFirst().getSkillType()).isEqualTo(SkillType.EDIT);
        assertThat(command.skills().getFirst().getContent()).isEqualTo("Spring Boot");

        assertThat(command.studio().getStudioName()).isEqualTo("스튜디오명");
        assertThat(command.studio().getEmployeesCount()).isEqualTo(5);
        assertThat(command.studio().getBusinessHours()).isEqualTo("10:00 ~ 18:00");
        assertThat(command.studio().getAddress()).isEqualTo("서울 강남구");

        assertThat(command.portfolioLinks()).hasSize(1);
        assertThat(command.portfolioLinks().getFirst()).isEqualTo("https://portfolio.site");
    }
}