package com.picus.core.expert.adapter.out.persistence;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.expert.adapter.out.persistence.mapper.ExpertPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.ProjectPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.SkillPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.mapper.StudioPersistenceMapper;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import jakarta.persistence.EntityManager;
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
    ExpertJpaRepository expertJpaRepository;

    @Autowired
    ProjectJpaRepository projectJpaRepository;

    @Autowired
    SkillJpaRepository skillJpaRepository;

    @Autowired
    StudioJpaRepository studioJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Expert 도메인 객체를 저장하면 연관된 Project, Skill, Studio도 함께 저장되며 기본키는 연관맺은 UserEntity의 기본키가 된다.")
    public void createExpert() throws Exception {
        // given
        UserEntity userEntity = givenUserEntity();
        userJpaRepository.save(userEntity);
        Expert expert = givenExpertDomain();

        // when
        Expert saved = expertPersistenceAdapter.create(expert, userEntity.getUserNo());

        // then
        Optional<ExpertEntity> optionalResult = expertJpaRepository.findById(saved.getExpertNo());
        assertThat(optionalResult).isPresent();

        // 기본키가 관계 맺은 UserEntity의 기본키와 같은지
        assertThat(optionalResult.get().getExpertNo()).isEqualTo(userEntity.getUserNo());

        assertThat(projectJpaRepository.findAll())
                .anyMatch(p -> p.getProjectName().equals("프로젝트 A"));

        assertThat(skillJpaRepository.findAll())
                .anyMatch(s -> s.getContent().equals("카메라 전문가"));

        assertThat(studioJpaRepository.findAll())
                .anyMatch(s -> s.getStudioName().equals("포토 스튜디오"));
    }

    @Test
    @DisplayName("expertNo로 Expert를 조회한다.")
    public void findById_success() throws Exception {
        // given
        UserEntity userEntity = givenUserEntity();
        ExpertEntity savedExpertEntity = settingTestExpertEntityData(userEntity);
        String savedExpertNo = savedExpertEntity.getExpertNo();

        // when
        Optional<Expert> optionalResult = expertPersistenceAdapter.findById(savedExpertNo);

        // then
        assertThat(optionalResult).isPresent();
        Expert expert = optionalResult.get();

        assertThat(expert.getExpertNo()).isEqualTo(savedExpertNo);
        assertThat(expert.getBackgroundImageKey()).isEqualTo("img-key");
        assertThat(expert.getIntro()).isEqualTo("전문가 소개");
        assertThat(expert.getActivityCareer()).isEqualTo("경력 5년");
        assertThat(expert.getActivityAreas()).containsExactly("서울 강북구");
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
        UserEntity userEntity = givenUserEntity();
        ExpertEntity expertEntity = settingTestExpertEntityData(userEntity);
        String expertNo = expertEntity.getExpertNo();

        clearPersistenceContext();

        // 수정할 도메인 Expert 생성
        Expert updatedExpert = Expert.builder()
                .expertNo(expertNo)
                .intro("수정된 소개")
                .activityCareer("수정된 경력")
                .activityAreas(List.of("서울 관악구"))
                .activityCount(15)
                .lastActivityAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .portfolioLinks(List.of("http://new-portfolio.com"))
                .approvalStatus(ApprovalStatus.APPROVAL)
                .build();

        // when
        expertPersistenceAdapter.update(updatedExpert);
        clearPersistenceContext();

        // then
        ExpertEntity updatedEntity = expertJpaRepository.findById(expertNo).orElseThrow();

        assertThat(updatedEntity.getIntro()).isEqualTo("수정된 소개");
        assertThat(updatedEntity.getActivityCareer()).isEqualTo("수정된 경력");
        assertThat(updatedEntity.getActivityAreas()).containsExactly("서울 관악구");
        assertThat(updatedEntity.getActivityCount()).isEqualTo(15);
        assertThat(updatedEntity.getLastActivityAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
        assertThat(updatedEntity.getPortfolioLinks()).containsExactly("http://new-portfolio.com");
        assertThat(updatedEntity.getApprovalStatus()).isEqualTo(ApprovalStatus.APPROVAL);

    }

    @Test
    @DisplayName("하나의 Expert의 ExpertEntity, ProjectEntity, SkillEntity, StudioEntity를 수정한다.")
    public void updateExpertWithDetail_success() throws Exception {
        // given
        UserEntity userEntity = givenUserEntity();
        ExpertEntity expertEntity = givenExpertEntity();
        expertEntity.bindUserEntity(userEntity);
        expertJpaRepository.save(expertEntity);

        List<ProjectEntity> projectEntities = givenProjectEntity(expertEntity);
        projectJpaRepository.saveAll(projectEntities);

        List<SkillEntity> skillEntities = givenSkillsEntity(expertEntity);
        skillJpaRepository.saveAll(skillEntities);

        StudioEntity studioEntity = givenStudioEntity(expertEntity);
        studioJpaRepository.save(studioEntity);

        String expertNo = expertEntity.getExpertNo();
        String updatedProjectNo = projectEntities.get(0).getProjectNo();
        String deletedProjectNo = projectEntities.get(1).getProjectNo();

        String updatedSkillNo = skillEntities.get(0).getSkillNo();
        String deletedSkillNo = skillEntities.get(1).getSkillNo();

        String updatedStudioNo = studioEntity.getStudioNo();

        clearPersistenceContext();

        // 수정할 Expert 도메인 준비
        Expert updatedExpert = Expert.builder()
                .expertNo(expertNo)
                .intro("수정된 소개")
                .activityCareer("수정된 경력")
                .activityAreas(List.of("서울 강남구", "부산 해운대"))
                .activityCount(20)
                .lastActivityAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .portfolioLinks(List.of("http://updated-portfolio.com"))
                .approvalStatus(ApprovalStatus.APPROVAL)
                .projects(List.of(
                        Project.builder() // 기존 프로젝트 수정
                                .projectNo(updatedProjectNo)
                                .projectName("수정된 프로젝트명")
                                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                                .endDate(LocalDateTime.of(2023, 12, 31, 18, 0))
                                .build(),
                        Project.builder() // 신규 프로젝트
                                .projectName("새 프로젝트")
                                .startDate(LocalDateTime.of(2024, 2, 1, 9, 0))
                                .endDate(LocalDateTime.of(2024, 3, 1, 18, 0))
                                .build()
                ))
                .skills(List.of(
                        Skill.builder() // 기존 스킬 수정
                                .skillNo(updatedSkillNo)
                                .skillType(SkillType.EDIT)
                                .content("편집 가능 (Final Cut)")
                                .build(),
                        Skill.builder() // 신규 스킬
                                .skillType(SkillType.LIGHT)
                                .content("LED 조명 운용")
                                .build()
                ))
                .studio(Studio.builder()
                        .studioNo(updatedStudioNo)
                        .studioName("업데이트된 스튜디오")
                        .employeesCount(10)
                        .businessHours("08:00~17:00")
                        .address("서울 용산구")
                        .build()
                )
                .build();

        // when
        expertPersistenceAdapter.update(updatedExpert,
                List.of(deletedProjectNo), List.of(deletedSkillNo), null);
        clearPersistenceContext();

        // then
        // Expert 검증
        ExpertEntity updatedEntity = expertJpaRepository.findById(expertNo).orElseThrow();
        assertThat(updatedEntity.getIntro()).isEqualTo("수정된 소개");
        assertThat(updatedEntity.getActivityCareer()).isEqualTo("수정된 경력");
        assertThat(updatedEntity.getActivityAreas()).containsExactly("서울 강남구", "부산 해운대");
        assertThat(updatedEntity.getActivityCount()).isEqualTo(20);
        assertThat(updatedEntity.getLastActivityAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
        assertThat(updatedEntity.getPortfolioLinks()).containsExactly("http://updated-portfolio.com");
        assertThat(updatedEntity.getApprovalStatus()).isEqualTo(ApprovalStatus.APPROVAL);

        // Project 검증
        List<ProjectEntity> projects = projectJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(projects).hasSize(2); // 기존 2개 + 신규 1개 - 삭제 1개
        assertThat(projects).extracting("projectName")
                .contains("수정된 프로젝트명", "새 프로젝트");

        // Skill 검증
        List<SkillEntity> skills = skillJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(skills).hasSize(2); // 기존 2개 + 신규 1개 - 삭제 1개
        assertThat(skills).extracting("skillType", "content")
                .contains(
                        tuple(SkillType.EDIT, "편집 가능 (Final Cut)"),
                        tuple(SkillType.LIGHT, "LED 조명 운용")
                );

        // Studio 검증
        StudioEntity studio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo).orElseThrow();
        assertThat(studio.getStudioName()).isEqualTo("업데이트된 스튜디오");
        assertThat(studio.getEmployeesCount()).isEqualTo(10);
        assertThat(studio.getBusinessHours()).isEqualTo("08:00~17:00");
        assertThat(studio.getAddress()).isEqualTo("서울 용산구");
    }

    @Test
    @DisplayName("하나의 Expert의 StudioEntity를 삭제한다.")
    public void updateExpertWithDetail_success_studio_delete() throws Exception {
        // given
        UserEntity userEntity = givenUserEntity();
        ExpertEntity expertEntity = givenExpertEntity();
        expertEntity.bindUserEntity(userEntity);
        expertJpaRepository.save(expertEntity);


        StudioEntity studioEntity = givenStudioEntity(expertEntity);
        studioJpaRepository.save(studioEntity);

        String expertNo = expertEntity.getExpertNo();

        String deletedStudioNo = studioEntity.getStudioNo();

        clearPersistenceContext();

        // 수정할 Expert 도메인 준비
        Expert updatedExpert = Expert.builder()
                .expertNo(expertNo)
                .build();

        // when
        expertPersistenceAdapter.update(updatedExpert,
                List.of(), List.of(), deletedStudioNo);
        clearPersistenceContext();

        // then
        Optional<StudioEntity> studio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(studio).isNotPresent();
    }


    /* 헬퍼 메서드 */
    private UserEntity givenUserEntity() {
        return UserEntity.builder()
                .name("이름")
                .nickname("닉네임")
                .tel("01012345678")
                .role(Role.CLIENT)
                .email("email@example.com")
                .providerId("social_abc123")
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .expertNo(null)
                .build();
    }

    private UserEntity givenUserEntityWithParam(String nickname, String name, String email, String providerId) {
        return UserEntity.builder()
                .name(name)
                .nickname(nickname)
                .tel("01012345678")
                .role(Role.CLIENT)
                .email(email)
                .providerId(providerId)
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .expertNo(null)
                .build();
    }

    private ExpertEntity settingTestExpertEntityData(UserEntity userEntity) {
        ExpertEntity expertEntity = givenExpertEntity();
        expertEntity.bindUserEntity(userEntity);
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
                .activityAreas(List.of("서울 강북구"))
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
                .activityAreas(List.of("서울 강북구"))
                .activityCount(10)
                .lastActivityAt(LocalDateTime.of(2024, 5, 10, 10, 0))
                .portfolioLinks(List.of("http://portfolio.com"))
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


    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}