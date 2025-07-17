package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ExpertPersistenceMapperTest {

    private final ExpertPersistenceMapper mapper = new ExpertPersistenceMapper();

    @Test
    @DisplayName("ExpertEntity 객체를 Expert 도메인으로 변환할 수 있다")
    void mapToDomain() {
        // Given
        ExpertEntity entity = givenExpertEntity();
        List<Project> projects = givenProjects();
        List<Skill> skills = givenSkills();
        Studio studio = givenStudio();


        // When
        Expert domain = mapper.mapToDomain(entity, projects, skills, studio);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getExpertNo()).isEqualTo("EXP456");
        assertThat(domain.getBackgroundImageKey()).isEqualTo("img-key");
        assertThat(domain.getIntro()).isEqualTo("전문가 소개");
        assertThat(domain.getActivityCareer()).isEqualTo("경력 5년");
        assertThat(domain.getActivityAreas()).containsExactly("서울 강북구");
        assertThat(domain.getActivityCount()).isEqualTo(8);
        assertThat(domain.getLastActivityAt()).isEqualTo(LocalDateTime.of(2024, 5, 20, 10, 30));
        assertThat(domain.getPortfolios()).extracting(Portfolio::getLink).containsExactly("http://myportfolio.com");
        assertThat(domain.getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
        assertThat(domain.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 5, 20, 10, 30));
        assertThat(domain.getUpdatedAt()).isEqualTo(LocalDateTime.of(2024, 5, 21, 10, 30));
        assertThat(domain.getDeletedAt()).isEqualTo(LocalDateTime.of(2024, 5, 22, 10, 30));
        // Projects
        assertThat(domain.getProjects()).hasSize(2)
                .extracting("projectName", "startDate", "endDate")
                .containsExactlyInAnyOrder(
                        tuple("단편영화 촬영 프로젝트",
                                LocalDateTime.of(2022, 5, 1, 0, 0),
                                LocalDateTime.of(2022, 8, 15, 0, 0)),
                        tuple("뮤직비디오 조명 작업",
                                LocalDateTime.of(2023, 1, 10, 0, 0),
                                LocalDateTime.of(2023, 2, 20, 0, 0))
                );

        // Skills
        assertThat(domain.getSkills()).hasSize(2)
                .extracting("skillType", "content")
                .containsExactlyInAnyOrder(
                        tuple(SkillType.CAMERA, "시네마 카메라 운용 가능 (RED, Blackmagic)"),
                        tuple(SkillType.EDIT, "프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                );

        // Studio
        assertThat(domain.getStudio()).isNotNull();
        assertThat(domain.getStudio())
                .extracting("studioName", "employeesCount", "businessHours", "address")
                .containsExactly("크리에이티브 필름", 5, "10:00 - 19:00", "서울특별시 마포구 월드컵북로 400");

    }

    @Test
    @DisplayName("Expert 도메인 객체를 ExpertEntity로 변환할 수 있다")
    void mapToEntity() {
        // Given
        Expert domain = givenExpertDomain();

        // When
        ExpertEntity entity = mapper.mapToEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getBackgroundImageKey()).isEqualTo("bg-key");
        assertThat(entity.getIntro()).isEqualTo("소개입니다");
        assertThat(entity.getActivityCareer()).isEqualTo("10년 경력");
        assertThat(entity.getActivityAreas()).containsExactly("서울 강북구");
        assertThat(entity.getActivityCount()).isEqualTo(15);
        assertThat(entity.getLastActivityAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(entity.getPortfolioLinks()).containsExactly("http://portfolio.com");
        assertThat(entity.getApprovalStatus()).isEqualTo(ApprovalStatus.APPROVAL);
    }

    private Expert givenExpertDomain() {
        Expert domain = Expert.builder()
                .backgroundImageKey("bg-key")
                .intro("소개입니다")
                .activityCareer("10년 경력")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(15)
                .lastActivityAt(LocalDateTime.of(2023, 1, 1, 12, 0))
                .portfolios(List.of(Portfolio.builder().link("http://portfolio.com").build()))
                .approvalStatus(ApprovalStatus.APPROVAL)
                .build();
        return domain;
    }

    private ExpertEntity givenExpertEntity() {
        return ExpertEntity.builder()
                .expertNo("EXP456")
                .backgroundImageKey("img-key")
                .intro("전문가 소개")
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(8)
                .lastActivityAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .createdAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .updatedAt(LocalDateTime.of(2024, 5, 21, 10, 30))
                .deletedAt(LocalDateTime.of(2024, 5, 22, 10, 30))
                .build();
    }

    private List<Project> givenProjects() {
        return List.of(
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
        );
    }

    private List<Skill> givenSkills() {
        return List.of(
                Skill.builder()
                        .skillType(SkillType.CAMERA)
                        .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                        .build(),
                Skill.builder()
                        .skillType(SkillType.EDIT)
                        .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                        .build()
        );
    }

    private Studio givenStudio() {
        return Studio.builder()
                .studioName("크리에이티브 필름")
                .employeesCount(5)
                .businessHours("10:00 - 19:00")
                .address("서울특별시 마포구 월드컵북로 400")
                .build();
    }
}
