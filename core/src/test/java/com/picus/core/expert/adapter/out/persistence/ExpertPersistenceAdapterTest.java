package com.picus.core.expert.adapter.out.persistence;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.ProjectPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.SkillPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.StudioPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import({
        ExpertPersistenceAdapter.class,
        ExpertPersistenceMapper.class,
        ProjectPersistenceMapper.class,
        SkillPersistenceMapper.class,
        StudioPersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpertPersistenceAdapterTest {

    @Autowired
    ExpertPersistenceAdapter expertPersistenceAdapter;

    @Autowired
    private ExpertJpaRepository expertJpaRepository;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private SkillJpaRepository skillJpaRepository;

    @Autowired
    private StudioJpaRepository studioJpaRepository;

    @Test
    @DisplayName("Expert 도메인 객체를 저장하면 연관된 Project, Skill, Studio도 함께 저장된다")
    public void saveExpert() throws Exception {
        // given
        Expert expert = givenExpertDomain();

        // when
        Expert saved = expertPersistenceAdapter.saveExpert(expert);

        // then
        assertThat(expertJpaRepository.findById(saved.getExpertNo())).isPresent();

        assertThat(projectJpaRepository.findAll())
                .anyMatch(p -> p.getProjectName().equals("프로젝트 A"));

        assertThat(skillJpaRepository.findAll())
                .anyMatch(s -> s.getContent().equals("카메라 전문가"));

        assertThat(studioJpaRepository.findAll())
                .anyMatch(s -> s.getStudioName().equals("포토 스튜디오"));
    }

    @Test
    @DisplayName("expertNo로 Expert를 조회한다.")
    public void loadExpertByExpertNo_success() throws Exception {
        // given
        ExpertEntity savedExpertEntity = settingDefaultEntityData();
        String savedExpertNo = savedExpertEntity.getExpertNo();

        // when
        Optional<Expert> optionalResult = expertPersistenceAdapter.loadExpertByExpertNo(savedExpertNo);

        // then
        assertThat(optionalResult).isPresent();
        Expert expert = optionalResult.get();

        assertThat(expert.getExpertNo()).isEqualTo(savedExpertNo);
        assertThat(expert.getBackgroundImageKey()).isEqualTo("img-key");
        assertThat(expert.getIntro()).isEqualTo("전문가 소개");
        assertThat(expert.getActivityCareer()).isEqualTo("경력 5년");
        assertThat(expert.getActivityAreas()).containsExactly(ActivityArea.SEOUL_GANGBUKGU);
        assertThat(expert.getActivityCount()).isEqualTo(8);
        assertThat(expert.getLastActivityAt()).isEqualTo(LocalDateTime.of(2024, 5, 20, 10, 30));
        assertThat(expert.getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);

        // projects 검증
        assertThat(expert.getProjects()).hasSize(2)
                .extracting("projectName", "startDate", "endDate")
                .containsExactlyInAnyOrder(
                        tuple("단편영화 촬영 프로젝트",
                                LocalDateTime.of(2022, 5, 1, 0, 0),
                                LocalDateTime.of(2022, 8, 15, 0, 0)),
                        tuple("뮤직비디오 조명 작업",
                                LocalDateTime.of(2023, 1, 10, 0, 0),
                                LocalDateTime.of(2023, 2, 20, 0, 0))
                );

        // skills 검증
        assertThat(expert.getSkills()).hasSize(2)
                .extracting("skillType", "content")
                .containsExactlyInAnyOrder(
                        tuple(SkillType.CAMERA, "시네마 카메라 운용 가능 (RED, Blackmagic)"),
                        tuple(SkillType.EDIT, "프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                );

        // studio 검증
        assertThat(expert.getStudio()).isNotNull();
        assertThat(expert.getStudio())
                .extracting("studioName", "employeesCount", "businessHours", "address")
                .containsExactly("크리에이티브 필름", 5, "10:00 - 19:00", "서울특별시 마포구 월드컵북로 400");
    }


    @Test
    @DisplayName("ExpertEntity를 수정한다.")
    public void updateExpert_success() throws Exception {
        // given
        ExpertEntity originalEntity = givenExpertEntity();
        ExpertEntity savedEntity = expertJpaRepository.save(originalEntity);
        String expertNo = savedEntity.getExpertNo();


        // 수정할 도메인 Expert 생성
        Expert updatedExpert = Expert.builder()
                .expertNo(expertNo)
                .intro("수정된 소개")
                .activityCareer("수정된 경력")
                .activityAreas(List.of(ActivityArea.SEOUL_GWANAKGU))
                .activityCount(15)
                .lastActivityAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .portfolios(List.of(Portfolio.builder().link("http://new-portfolio.com").build()))
                .approvalStatus(ApprovalStatus.APPROVAL)
                .build();

        // when
        expertPersistenceAdapter.updateExpert(updatedExpert);

        // then
        ExpertEntity updatedEntity = expertJpaRepository.findById(expertNo).orElseThrow();
        assertThat(updatedEntity).satisfies(expertEntity -> {
            assertThat(expertEntity.getIntro()).isEqualTo("수정된 소개");
            assertThat(expertEntity.getActivityCareer()).isEqualTo("수정된 경력");
            assertThat(expertEntity.getActivityAreas()).containsExactly(ActivityArea.SEOUL_GWANAKGU);
            assertThat(expertEntity.getActivityCount()).isEqualTo(15);
            assertThat(expertEntity.getLastActivityAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
            assertThat(expertEntity.getPortfolioLinks()).containsExactly("http://new-portfolio.com");
            assertThat(expertEntity.getApprovalStatus()).isEqualTo(ApprovalStatus.APPROVAL);
        });

    }


    private ExpertEntity settingDefaultEntityData() {
        ExpertEntity expertEntity = givenExpertEntity();
        ExpertEntity savedExpertEntity = expertJpaRepository.save(expertEntity);

        List<ProjectEntity> projectEntities = givenProjectEntity(savedExpertEntity);
        projectJpaRepository.saveAll(projectEntities);

        List<SkillEntity> skillEntities = givenSkillsEntity(savedExpertEntity);
        skillJpaRepository.saveAll(skillEntities);

        StudioEntity studioEntity = givenStudioEntity(savedExpertEntity);
        studioJpaRepository.save(studioEntity);
        return savedExpertEntity;
    }

    private ExpertEntity givenExpertEntity() {
        return ExpertEntity.builder()
                .backgroundImageKey("img-key")
                .intro("전문가 소개")
                .activityCareer("경력 5년")
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU))
                .activityCount(8)
                .lastActivityAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }

    private List<ProjectEntity> givenProjectEntity(ExpertEntity expertEntity) {
        return List.of(
                ProjectEntity.builder()
                        .expertEntity(expertEntity)
                        .projectName("단편영화 촬영 프로젝트")
                        .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                        .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                        .build(),
                ProjectEntity.builder()
                        .expertEntity(expertEntity)
                        .projectName("뮤직비디오 조명 작업")
                        .startDate(LocalDateTime.of(2023, 1, 10, 0, 0))
                        .endDate(LocalDateTime.of(2023, 2, 20, 0, 0))
                        .build()
        );
    }

    private List<SkillEntity> givenSkillsEntity(ExpertEntity expertEntity) {
        return List.of(
                SkillEntity.builder()
                        .expertEntity(expertEntity)
                        .skillType(SkillType.CAMERA)
                        .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                        .build(),
                SkillEntity.builder()
                        .expertEntity(expertEntity)
                        .skillType(SkillType.EDIT)
                        .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                        .build()
        );
    }

    private StudioEntity givenStudioEntity(ExpertEntity expertEntity) {
        return StudioEntity.builder()
                .expertEntity(expertEntity)
                .studioName("크리에이티브 필름")
                .employeesCount(5)
                .businessHours("10:00 - 19:00")
                .address("서울특별시 마포구 월드컵북로 400")
                .build();
    }

    private Expert givenExpertDomain() {
        return Expert.builder()
                .intro("소개입니다")
                .activityCareer("5년")
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU))
                .activityCount(10)
                .lastActivityAt(LocalDateTime.of(2024, 5, 10, 10, 0))
                .portfolios(List.of(Portfolio.builder().link("http://portfolio.com").build()))
                .approvalStatus(ApprovalStatus.PENDING)
                .projects(List.of(
                        Project.builder()
                                .projectName("프로젝트 A")
                                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                                .endDate(LocalDateTime.of(2023, 12, 31, 18, 0))
                                .build()
                ))
                .skills(List.of(
                        Skill.builder()
                                .skillType(SkillType.CAMERA)
                                .content("카메라 전문가")
                                .build()
                ))
                .studio(
                        Studio.builder()
                                .studioName("포토 스튜디오")
                                .employeesCount(3)
                                .businessHours("09:00~18:00")
                                .address("서울 강남구")
                                .build()
                )
                .build();
    }

}