package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.LoadExpertBasicInfoResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse;
import com.picus.core.expert.application.port.in.result.ExpertBasicInfoResult;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoadExpertWebMapperTest {

    private final LoadExpertWebMapper mapper = new LoadExpertWebMapper();

    @Test
    @DisplayName("Expert 도메인 객체를 기본 정보 WebResponse로 매핑한다")
    void toBasicInfo_Response_shouldMapCorrectly() {
        // given
        ExpertBasicInfoResult appResponse = ExpertBasicInfoResult.builder()
                .expertNo("expert_no")
                .activityDuration("3년")
                .activityCount(12)
                .lastActivityAt(LocalDateTime.of(2024, 7, 1, 15, 0))
                .intro("소개입니다")
                .backgroundImageUrl("https://image.com/bg.png")
                .nickname("전문가닉네임")
                .profileImageUrl("https://image.com/profile.png")
                .links(List.of("https://insta.com"))
                .build();

        // when
        LoadExpertBasicInfoResponse result = mapper.toBasicInfoResponse(appResponse);

        // then
        assertThat(result).isNotNull();
        assertThat(result.expertNo()).isEqualTo("expert_no");
        assertThat(result.activityDuration()).isEqualTo("3년");
        assertThat(result.activityCount()).isEqualTo(12);
        assertThat(result.lastActivityAt()).isEqualTo(LocalDateTime.of(2024, 7, 1, 15, 0));
        assertThat(result.intro()).isEqualTo("소개입니다");
        assertThat(result.backgroundImageUrl()).isEqualTo("https://image.com/bg.png");
        assertThat(result.nickname()).isEqualTo("전문가닉네임");
        assertThat(result.profileImageUrl()).isEqualTo("https://image.com/profile.png");
        assertThat(result.links()).isEqualTo(List.of("https://insta.com"));
    }

    @Test
    @DisplayName("Expert 도메인 객체를 상세 정보 WebResponse로 매핑한다")
    void toDetailInfo_Response_shouldMapCorrectly() {
        // given

        List<Project> projects = List.of(Project.builder()
                .projectName("프로젝트1")
                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2023, 6, 1, 18, 0))
                .build());

        List<Skill> skills = List.of(Skill.builder()
                .skillType(SkillType.EDIT)
                .content("Java")
                .build());

        List<String> activityAreas = List.of("서울", "부산");

        Studio studio = Studio.builder()
                .studioName("스튜디오A")
                .employeesCount(3)
                .businessHours("10-6")
                .address("강남")
                .build();

        Expert expert = Expert.builder()
                .activityCareer("5년차 백엔드 개발자")
                .projects(projects)
                .skills(skills)
                .activityAreas(activityAreas)
                .studio(studio)
                .build();

        // when
        LoadExpertDetailInfoResponse response = mapper.toDetailInfoResponse(expert);

        // then
        assertThat(response.activityCareer()).isEqualTo("5년차 백엔드 개발자");

        assertThat(response.projects()).hasSize(1);
        assertThat(response.projects().get(0).projectName()).isEqualTo("프로젝트1");
        assertThat(response.projects().get(0).startDate()).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertThat(response.projects().get(0).endDate()).isEqualTo(LocalDateTime.of(2023, 6, 1, 18, 0));

        assertThat(response.skills()).hasSize(1);
        assertThat(response.skills().get(0).skillType()).isEqualTo(SkillType.EDIT);
        assertThat(response.skills().get(0).content()).isEqualTo("Java");

        assertThat(response.activityAreas()).containsExactly("서울", "부산");

        assertThat(response.studio().studioName()).isEqualTo("스튜디오A");
        assertThat(response.studio().employeesCount()).isEqualTo(3);
        assertThat(response.studio().businessHours()).isEqualTo("10-6");
        assertThat(response.studio().address()).isEqualTo("강남");
    }
}