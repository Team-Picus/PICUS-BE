package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.domain.model.*;
import com.picus.core.expert.domain.model.vo.*;
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
        Expert expert = Expert.builder()
                .intro("자기소개입니다")
                .activityCount(5)
                .lastActivityAt(LocalDateTime.of(2024, 12, 10, 10, 0))
                .backgroundImageUrl("https://cdn.com/bg.jpg")
                .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0)) // 활동 기간 계산용
                .build();

        // when
        GetExpertBasicInfoWebResponse response = mapper.toBasicInfo(expert);

        // then
        assertThat(response.activityCount()).isEqualTo(5);
        assertThat(response.lastActivityAt()).isEqualTo(LocalDateTime.of(2024, 12, 10, 10, 0));
        assertThat(response.intro()).isEqualTo("자기소개입니다");
        assertThat(response.backgroundImageUrl()).isEqualTo("https://cdn.com/bg.jpg");
        assertThat(response.activityDuration()).isNotNull(); // 실제 계산값은 날짜 기준이므로 생략
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