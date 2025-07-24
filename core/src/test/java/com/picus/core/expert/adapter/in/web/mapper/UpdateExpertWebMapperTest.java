package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.SkillWebRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoWebRequest.StudioWebRequest;
import com.picus.core.expert.application.port.in.command.*;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.expert.application.port.in.command.ChangeStatus.*;
import static org.assertj.core.api.Assertions.assertThat;


class UpdateExpertWebMapperTest {

    private final UpdateExpertWebMapper mapper = new UpdateExpertWebMapper();

    @Test
    @DisplayName("ExpertBasicInfoCommandWebRequest를 ExpertBasicInfoCommandRequest로 변환한다.")
    void toBasicInfoAppRequest_should_map_all_fields_correctly() {
        // given
        String currentUserNo = "USER123";
        UpdateExpertBasicInfoWebRequest webRequest = new UpdateExpertBasicInfoWebRequest(
                "profile-img-key",
                "background-img-key",
                "test-nickname",
                List.of("https://my.link"),
                "소개 글입니다"
        );

        // when
        UpdateExpertBasicInfoAppRequest result = mapper.toBasicInfoAppRequest(webRequest, currentUserNo);

        // then
        assertThat(result.currentUserNo()).isEqualTo("USER123");
        assertThat(result.profileImageFileKey()).isEqualTo("profile-img-key");
        assertThat(result.backgroundImageFileKey()).isEqualTo("background-img-key");
        assertThat(result.nickname()).isEqualTo("test-nickname");
        assertThat(result.link()).isEqualTo(List.of("https://my.link"));
        assertThat(result.intro()).isEqualTo("소개 글입니다");
    }

    @Test
    @DisplayName("toDetailInfoAppRequest: 웹 요청 DTO가 도메인 요청 DTO로 정확히 변환된다.")
    void toDetailInfoAppRequest_success() {
        // given
        String currentUserNo = "USER123";

        UpdateExpertDetailInfoWebRequest webRequest = UpdateExpertDetailInfoWebRequest.builder()
                .activityCareer("촬영 경력 3년")
                .activityAreas(List.of("서울시 강남구", "경기도 성남시"))
                .projects(List.of(
                        ProjectWebRequest.builder()
                                .projectNo("P001")
                                .projectName("단편영화 제작")
                                .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                                .endDate(LocalDateTime.of(2023, 3, 1, 0, 0))
                                .changeStatus(NEW)
                                .build()
                ))
                .skills(List.of(
                        SkillWebRequest.builder()
                                .skillNo("S001")
                                .skillType("CAMERA")
                                .content("블랙매직 URSA Mini Pro 사용 가능")
                                .changeStatus(UPDATE)
                                .build()
                ))
                .studio(StudioWebRequest.builder()
                        .studioNo("ST001")
                        .studioName("필름 하우스")
                        .employeesCount(4)
                        .businessHours("09:00 ~ 18:00")
                        .address("서울특별시 용산구")
                        .changeStatus(DELETE)
                        .build())
                .build();


        // when
        UpdateExpertDetailInfoAppRequest result = mapper.toDetailInfoAppRequest(webRequest, currentUserNo);

        // then
        assertThat(result.currentUserNo()).isEqualTo(currentUserNo);
        assertThat(result.activityCareer()).isEqualTo("촬영 경력 3년");
        assertThat(result.activityAreas()).containsExactly("서울시 강남구", "경기도 성남시");

        assertThat(result.projects()).hasSize(1);
        ProjectCommand projectCommand = result.projects().getFirst();
        assertThat(projectCommand.projectNo()).isEqualTo("P001");
        assertThat(projectCommand.projectName()).isEqualTo("단편영화 제작");
        assertThat(projectCommand.startDate()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0));
        assertThat(projectCommand.endDate()).isEqualTo(LocalDateTime.of(2023, 3, 1, 0, 0));
        assertThat(projectCommand.changeStatus()).isEqualTo(NEW);

        assertThat(result.skills()).hasSize(1);
        SkillCommand skillCommand = result.skills().getFirst();
        assertThat(skillCommand.skillNo()).isEqualTo("S001");
        assertThat(skillCommand.skillType()).isEqualTo(SkillType.CAMERA);
        assertThat(skillCommand.content()).isEqualTo("블랙매직 URSA Mini Pro 사용 가능");
        assertThat(skillCommand.changeStatus()).isEqualTo(UPDATE);

        StudioCommand studioCommand = result.studio();
        assertThat(studioCommand.studioNo()).isEqualTo("ST001");
        assertThat(studioCommand.studioName()).isEqualTo("필름 하우스");
        assertThat(studioCommand.employeesCount()).isEqualTo(4);
        assertThat(studioCommand.businessHours()).isEqualTo("09:00 ~ 18:00");
        assertThat(studioCommand.address()).isEqualTo("서울특별시 용산구");
        assertThat(studioCommand.changeStatus()).isEqualTo(DELETE);
    }
}