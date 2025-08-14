package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.response.LoadExpertBasicInfoResponse;
import com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.IntegrationTestSupport;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.expert.adapter.in.web.data.response.LoadExpertDetailInfoResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LoadExpertIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ExpertJpaRepository expertJpaRepository;
    @Autowired
    private ProjectJpaRepository projectJpaRepository;
    @Autowired
    private SkillJpaRepository skillJpaRepository;
    @Autowired
    private StudioJpaRepository studioJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProfileImageJpaRepository profileImageJpaRepository;

    @AfterEach
    void tearDown() {
        projectJpaRepository.deleteAllInBatch();
        skillJpaRepository.deleteAllInBatch();
        studioJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        profileImageJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 전문가의 기본정보를 조회할 수 있다.")
    public void getExpertBasicInfo() throws Exception {
        // given
        String testNickname = "nick1"; // 테스트 데이터 셋팅
        String testProfileImageFileKey = "profile_key";
        int testActivityCount = 8;
        LocalDateTime testLastActivityAt = LocalDateTime.of(2024, 5, 20, 10, 30);
        String testIntro = "소개";
        String testBackgroundImageKey = "back_key";
        List<String> testLinks = List.of("link");

        UserEntity userEntity = createUserEntity(testNickname);
        ExpertEntity expertEntity = settingBasicData(
                userEntity,
                testProfileImageFileKey,
                testBackgroundImageKey,
                testIntro,
                testActivityCount,
                testLastActivityAt,
                testLinks
        );
        String expertNo = expertEntity.getExpertNo();

        commitTestTransaction();

        // 요청 셋팅
        HttpEntity<Void> request = settingWebRequest(userEntity, null);

        // when
        ResponseEntity<BaseResponse<LoadExpertBasicInfoResponse>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/basic_info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                expertNo
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<LoadExpertBasicInfoResponse> body = response.getBody();
        assertThat(body).isNotNull();

        LoadExpertBasicInfoResponse result = body.getResult();

        assertThat(result.expertNo()).isEqualTo(expertNo);
        assertThat(result.activityDuration()).isNotNull(); // 현재 시간 기반으로 계산되는거라 정확한 값 검증 힘듦
        assertThat(result.activityCount()).isEqualTo(testActivityCount);
        assertThat(result.lastActivityAt()).isEqualTo(testLastActivityAt);
        assertThat(result.intro()).isEqualTo(testIntro);
        assertThat(result.nickname()).isEqualTo(testNickname);
        assertThat(result.links()).isEqualTo(testLinks);
        // TODO: backgroundImageUrl, profileImageUrl 검증
    }


    @Test
    @DisplayName("사용자는 전문가의 상세정보를 조회할 수 있다.")
    public void getExpertDetailInfo() throws Exception {
        // given
        // 테스트 데이터 셋팅

        List<Project> testProjects = List.of(
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

        List<Skill> testSkills = List.of(
                Skill.builder()
                        .skillType(SkillType.CAMERA)
                        .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                        .build(),
                Skill.builder()
                        .skillType(SkillType.EDIT)
                        .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                        .build()
        );

        Studio testStudio = Studio.builder()
                .studioName("크리에이티브 필름")
                .employeesCount(5)
                .businessHours("10:00 - 19:00")
                .address("서울특별시 마포구 월드컵북로 400")
                .build();

        UserEntity userEntity = createUserEntity(); // UserEntity 저장
        SettingDetailDataResult settingDetailDataResult = settingDetailData(
                userEntity, "경력 5년", List.of("서울 강북구"), testProjects, testSkills, testStudio);

        commitTestTransaction();

        // 응답값 셋팅
        HttpEntity<Void> request = settingWebRequest(userEntity, null);

        // when
        ResponseEntity<BaseResponse<LoadExpertDetailInfoResponse>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/detail_info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                settingDetailDataResult.expertNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<LoadExpertDetailInfoResponse> body = response.getBody();
        assertThat(body).isNotNull();

        LoadExpertDetailInfoResponse result = body.getResult();

        assertThat(result.activityCareer()).isEqualTo("경력 5년");
        assertThat(result.activityAreas()).isEqualTo(List.of("서울 강북구"));

        // Projects
        List<ProjectResponse> projectResponses = result.projects();
        assertThat(projectResponses).hasSize(2)
                .extracting(
                        ProjectResponse::projectName,
                        ProjectResponse::startDate,
                        ProjectResponse::endDate
                )
                .containsExactlyInAnyOrder(
                        tuple(
                                testProjects.get(0).getProjectName(),
                                testProjects.get(0).getStartDate(),
                                testProjects.get(0).getEndDate()
                        ),
                        tuple(
                                testProjects.get(1).getProjectName(),
                                testProjects.get(1).getStartDate(),
                                testProjects.get(1).getEndDate()
                        )
                );
        assertThat(projectResponses)
                .extracting(ProjectResponse::projectNo)
                .containsExactlyInAnyOrder(settingDetailDataResult.projectNos.get(0), settingDetailDataResult.projectNos.get(1));

        // Skills
        List<SkillResponse> skillResponses = result.skills();
        assertThat(skillResponses).hasSize(2)
                .extracting(
                        SkillResponse::skillType,
                        SkillResponse::content
                )
                .containsExactlyInAnyOrder(
                        tuple(
                                testSkills.get(0).getSkillType(),
                                testSkills.get(0).getContent()),
                        tuple(
                                testSkills.get(1).getSkillType(),
                                testSkills.get(1).getContent())
                );
        assertThat(skillResponses)
                .extracting(SkillResponse::skillNo)
                .containsExactlyInAnyOrder(settingDetailDataResult.skillNos.get(0), settingDetailDataResult.skillNos.get(1));

        // Studio
        StudioResponse studioResponse = result.studio();
        assertThat(studioResponse).isNotNull();
        assertThat(studioResponse)
                .extracting(
                        StudioResponse::studioNo,
                        StudioResponse::studioName,
                        StudioResponse::employeesCount,
                        StudioResponse::businessHours,
                        StudioResponse::address
                )
                .containsExactly(
                        settingDetailDataResult.studioNo,
                        testStudio.getStudioName(),
                        testStudio.getEmployeesCount(),
                        testStudio.getBusinessHours(),
                        testStudio.getAddress()
                );
    }

    /**
     * private 메서드
     */
    private ExpertEntity settingBasicData(
            UserEntity userEntity,
            String testProfileImageFileKey,
            String testBackgroundImageKey,
            String testIntro,
            int testActivityCount,
            LocalDateTime testLastActivityAt,
            List<String> links) {

        ProfileImageEntity profileImageEntity =
                givenProfileImageEntity(testProfileImageFileKey, userEntity.getUserNo());
        profileImageJpaRepository.save(profileImageEntity);

        ExpertEntity expertEntity =
                givenExpertEntity(testBackgroundImageKey, testIntro, testActivityCount, testLastActivityAt, links);
        expertEntity.bindUserEntity(userEntity);
        expertJpaRepository.save(expertEntity);

        userEntity.assignExpertNo(expertEntity.getExpertNo());
        return expertEntity;
    }


    private record SettingDetailDataResult(
            String expertNo,
            List<String> projectNos,
            List<String> skillNos,
            String studioNo
    ) {
    }

    private SettingDetailDataResult settingDetailData(
            UserEntity userEntity,
            String testActivityCareer,
            List<String> testActivityAreas,
            List<Project> testProjects,
            List<Skill> testSkills,
            Studio testStudio
    ) {

        ExpertEntity expertEntity = givenExpertEntity(testActivityCareer, testActivityAreas);// Expert 저장
        expertEntity.bindUserEntity(userEntity);
        expertJpaRepository.save(expertEntity);

        List<ProjectEntity> projectEntities = givenProjectEntity(expertEntity, testProjects); // Project 저장
        projectJpaRepository.saveAll(projectEntities);

        List<SkillEntity> skillEntities = givenSkillsEntity(expertEntity, testSkills); // Skill 저장
        skillJpaRepository.saveAll(skillEntities);

        StudioEntity studioEntity = givenStudioEntity(expertEntity, testStudio); // Studio 저장
        studioJpaRepository.save(studioEntity);

        return new SettingDetailDataResult(
                expertEntity.getExpertNo(),
                projectEntities.stream().map(ProjectEntity::getProjectNo).toList(),
                skillEntities.stream().map(SkillEntity::getSkillNo).toList(),
                studioEntity.getStudioNo()
        );
    }

    private UserEntity createUserEntity(String nickname) {
        UserEntity userEntity = UserEntity.builder()
                .name("이름")
                .nickname(nickname)
                .tel("01012345678")
                .role(Role.CLIENT)
                .email("email@example.com")
                .providerId("social_abc123")
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .expertNo("expert-123")
                .build();
        return userJpaRepository.save(userEntity);
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .name("이름")
                .nickname("nickname")
                .tel("01012345678")
                .role(Role.CLIENT)
                .email("email@example.com")
                .providerId("social_abc123")
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .expertNo("expert-123")
                .build();
        return userJpaRepository.save(userEntity);
    }

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

    private UserEntity givenUserEntity(String nickname) {
        return UserEntity.builder()
                .name("이름")
                .nickname(nickname)
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

    private ExpertEntity givenExpertEntity(String activityCareer, List<String> activityAreas) {
        return ExpertEntity.builder()
                .activityCareer(activityCareer)
                .activityAreas(activityAreas)
                .intro("전문가 소개")
                .activityCount(8)
                .lastActivityAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }

    private ProfileImageEntity givenProfileImageEntity(String fileKey, String userNo) {
        return ProfileImageEntity.builder()
                .file_key(fileKey)
                .userNo(userNo)
                .build();
    }

    private ExpertEntity givenExpertEntity(String backgroundImageKey, String intro, int activityCount, LocalDateTime lastActivityAt, List<String> portfolioLinks) {
        return ExpertEntity.builder()
                .backgroundImageKey(backgroundImageKey)
                .intro(intro)
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(activityCount)
                .lastActivityAt(lastActivityAt)
                .portfolioLinks(portfolioLinks)
                .approvalStatus(ApprovalStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    private List<ProjectEntity> givenProjectEntity(ExpertEntity expertEntity, List<Project> projects) {
        return projects.stream()
                .map(p -> ProjectEntity.builder()
                        .expertEntity(expertEntity)
                        .projectName(p.getProjectName())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .build())
                .toList();
    }

    private List<SkillEntity> givenSkillsEntity(ExpertEntity expertEntity, List<Skill> skills) {
        return skills.stream()
                .map(s -> SkillEntity.builder()
                        .expertEntity(expertEntity)
                        .skillType(s.getSkillType())
                        .content(s.getContent())
                        .build())
                .toList();
    }

    private StudioEntity givenStudioEntity(ExpertEntity expertEntity, Studio studio) {
        return StudioEntity.builder()
                .expertEntity(expertEntity)
                .studioName(studio.getStudioName())
                .employeesCount(studio.getEmployeesCount())
                .businessHours(studio.getBusinessHours())
                .address(studio.getAddress())
                .build();
    }
}
