package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.application.port.in.response.GetExpertBasicInfoAppResponse;
import com.picus.core.expert.domain.model.*;
import com.picus.core.expert.domain.model.vo.*;
import com.picus.core.user.application.port.out.response.UserWithProfileImageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetExpertWebMapperTest {

    private final GetExpertWebMapper mapper = new GetExpertWebMapper();

    @Test
    @DisplayName("Expert 도메인 객체를 기본 정보 WebResponse로 매핑한다")
    void toBasicInfo_shouldMapCorrectly() {
        // given
        GetExpertBasicInfoAppResponse appResponse = GetExpertBasicInfoAppResponse.builder()
                .activityDuration("3년")
                .activityCount(12)
                .lastActivityAt(LocalDateTime.of(2024, 7, 1, 15, 0))
                .intro("소개입니다")
                .backgroundImageUrl("https://image.com/bg.png")
                .nickname("전문가닉네임")
                .profileImageUrl("https://image.com/profile.png")
                .build();

        // when
        GetExpertBasicInfoWebResponse result = mapper.toBasicInfo(appResponse);

        // then
        assertThat(result).isNotNull();
        assertThat(result.activityDuration()).isEqualTo("3년");
        assertThat(result.activityCount()).isEqualTo(12);
        assertThat(result.lastActivityAt()).isEqualTo(LocalDateTime.of(2024, 7, 1, 15, 0));
        assertThat(result.intro()).isEqualTo("소개입니다");
        assertThat(result.backgroundImageUrl()).isEqualTo("https://image.com/bg.png");
        assertThat(result.nickname()).isEqualTo("전문가닉네임");
        assertThat(result.profileImageUrl()).isEqualTo("https://image.com/profile.png");
    }

    @Test
    @DisplayName("Expert 도메인 객체를 상세 정보 WebResponse로 매핑한다")
    void toDetailInfo_shouldMapCorrectly() {
        // given
        List<Project> projects = List.of(Project.builder().projectName("프로젝트1").build());
        List<Skill> skills = List.of(Skill.builder().content("Java").skillType(SkillType.EDIT).build());
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
        GetExpertDetailInfoWebResponse response = mapper.toDetailInfo(expert);

        // then
        assertThat(response.activityCareer()).isEqualTo("5년차 백엔드 개발자");
        assertThat(response.projects()).isEqualTo(projects);
        assertThat(response.skills()).isEqualTo(skills);
        assertThat(response.activityAreas()).containsExactly("서울", "부산");
        assertThat(response.studio()).isEqualTo(studio);
    }
}