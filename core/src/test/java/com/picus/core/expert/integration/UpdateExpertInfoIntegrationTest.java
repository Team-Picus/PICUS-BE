package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest;
import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.ProjectWebRequest;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
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
import java.util.Optional;

import static com.picus.core.expert.adapter.in.web.data.request.UpdateExpertDetailInfoRequest.*;
import static com.picus.core.expert.application.port.in.command.ChangeStatus.*;
import static com.picus.core.expert.domain.vo.SkillType.EDIT;
import static com.picus.core.expert.domain.vo.SkillType.LIGHT;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class UpdateExpertInfoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProfileImageJpaRepository profileImageJpaRepository;
    @Autowired
    private ExpertJpaRepository expertJpaRepository;
    @Autowired
    private ProjectJpaRepository projectJpaRepository;
    @Autowired
    private SkillJpaRepository skillJpaRepository;
    @Autowired
    private StudioJpaRepository studioJpaRepository;

    @AfterEach
    void tearDown() {
        studioJpaRepository.deleteAllInBatch();
        skillJpaRepository.deleteAllInBatch();
        projectJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        profileImageJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 전문가의 기본정보를 수정할 수 있다.")
    public void updateExpertBasicInfo() throws Exception {
        // given
        // 테스트 데이터 셋팅
        UserEntity userEntity = givenUserEntity("old_nick");
        userJpaRepository.save(userEntity);
        String userNo = userEntity.getUserNo();

        ProfileImageEntity profileImageEntity = givenProfileImageEntity("old_profile_key", userNo);
        profileImageJpaRepository.save(profileImageEntity);
        String profileImageNo = profileImageEntity.getProfileImageNo();

        ExpertEntity expertEntity = givenExpertEntity("old_back_key", "old_intro", "old_link", userEntity);
        expertJpaRepository.save(expertEntity);
        String expertNo = expertEntity.getExpertNo();
        userEntity.assignExpertNo(expertNo);

        commitTestTransaction();

        // 요청 셋팅
        HttpEntity<UpdateExpertBasicInfoRequest> request = settingWebRequest(userEntity, givenWebRequest(
                "new_profile_key",
                "new_back_key",
                "new_nick",
                List.of("new_link"),
                "new_intro"));

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/basic_info",
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 닉네임 변경 검증
        Optional<UserEntity> optionalUserResult = userJpaRepository.findById(userNo);
        assertThat(optionalUserResult).isPresent();
        UserEntity userResult = optionalUserResult.get();
        assertThat(userResult.getNickname()).isEqualTo("new_nick");

        // 프로필 이미지 파일키 변경 검증
        Optional<ProfileImageEntity> optionalProfileResult = profileImageJpaRepository.findById(profileImageNo);
        assertThat(optionalProfileResult).isPresent();
        ProfileImageEntity profileResult = optionalProfileResult.get();
        assertThat(profileResult.getFile_key()).isEqualTo("new_profile_key");

        // 전문가 정보 검증
        Optional<ExpertEntity> optionalExpertResult = expertJpaRepository.findById(expertNo);
        assertThat(optionalExpertResult).isPresent();
        ExpertEntity expertResult = optionalExpertResult.get();
        assertThat(expertResult).extracting(
                ExpertEntity::getBackgroundImageKey,
                ExpertEntity::getPortfolioLinks,
                ExpertEntity::getIntro
        ).containsExactly("new_back_key", List.of("new_link"), "new_intro");
    }

    @Test
    @DisplayName("사용자는 전문가의 상세정보를 수정할 수 있다.")
    public void updateExpertDetailInfo() throws Exception {
        // given
        // 테스트 데이터 셋팅
        UserEntity userEntity = givenUserEntity();
        userJpaRepository.save(userEntity);
        String userNo = userEntity.getUserNo();

        ExpertEntity expertEntity = givenExpertEntity("수정 전 경력", List.of("수정 전 지역"), userEntity);
        expertJpaRepository.save(expertEntity);
        String expertNo = expertEntity.getExpertNo();

        userEntity.assignExpertNo(expertNo);

        ProjectEntity shdUptProjectEntity = givenProjectEntity(expertEntity, "수정 전 프로젝트명");
        ProjectEntity shddelProjectEntity = givenProjectEntity(expertEntity, "삭제 할 프로젝트명");
        projectJpaRepository.saveAll(List.of(shdUptProjectEntity, shddelProjectEntity));

        SkillEntity shdUptskillEntity = givenSkillEntity(expertEntity, SkillType.CAMERA, "수정 전 내용");
        SkillEntity shdDelSkillEntity = givenSkillEntity(expertEntity, SkillType.CAMERA, "수정 전 내용");
        skillJpaRepository.saveAll(List.of(shdUptskillEntity, shdDelSkillEntity));

        StudioEntity shdUptStudioEntity = givenStudioEntity(expertEntity,
                "수정될 스튜디오명", 5, "10:00 - 19:00", "서울시 은평구");
        studioJpaRepository.save(shdUptStudioEntity);

        commitTestTransaction();

        // 요청 셋팅
        UpdateExpertDetailInfoRequest request = UpdateExpertDetailInfoRequest.builder()
                .activityCareer("수정 후 경력")
                .activityAreas(List.of("수정 후 지역", "새로운 지역"))
                .projects(List.of(
                        ProjectWebRequest.builder()
                                .projectNo(shdUptProjectEntity.getProjectNo())
                                .projectName("수정된 프로젝트명")
                                .startDate(shdUptProjectEntity.getStartDate())
                                .endDate(shdUptProjectEntity.getEndDate())
                                .changeStatus(UPDATE)
                                .build(),
                        ProjectWebRequest.builder()
                                .projectName("새로운 프로젝트명")
                                .startDate(LocalDateTime.of(2025, 6, 6, 12, 0))
                                .endDate(LocalDateTime.of(2025, 6, 6, 12, 15))
                                .changeStatus(NEW)
                                .build(),
                        ProjectWebRequest.builder()
                                .projectNo(shddelProjectEntity.getProjectNo())
                                .projectName(shddelProjectEntity.getProjectName())
                                .startDate(shddelProjectEntity.getStartDate())
                                .endDate(shddelProjectEntity.getEndDate())
                                .changeStatus(DELETE)
                                .build()
                ))
                .skills(List.of(
                                SkillWebRequest.builder()
                                        .skillNo(shdUptskillEntity.getSkillNo())
                                        .skillType(LIGHT)
                                        .content("수정된 내용")
                                        .changeStatus(UPDATE)
                                        .build(),
                                SkillWebRequest.builder()
                                        .skillType(EDIT)
                                        .content("새로운 내용")
                                        .changeStatus(NEW)
                                        .build(),
                                SkillWebRequest.builder()
                                        .skillNo(shdDelSkillEntity.getSkillNo())
                                        .skillType(shdDelSkillEntity.getSkillType())
                                        .content(shdDelSkillEntity.getContent())
                                        .changeStatus(DELETE)
                                        .build()
                        )
                )
                .studio(
                        StudioWebRequest.builder()
                                .studioNo(shdUptStudioEntity.getStudioNo())
                                .studioName("수정된 이름")
                                .employeesCount(10)
                                .businessHours(shdUptStudioEntity.getBusinessHours())
                                .address(shdUptStudioEntity.getAddress())
                                .changeStatus(UPDATE)
                                .build()
                )
                .build();
        HttpEntity<UpdateExpertDetailInfoRequest> httpEntity = settingWebRequest(userEntity, request);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/detail_info",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Expert 검증
        Optional<ExpertEntity> optionalUpdatedExpert = expertJpaRepository.findById(expertNo);
        assertThat(optionalUpdatedExpert).isPresent();
        ExpertEntity updatedExpert = optionalUpdatedExpert.get();
        assertThat(updatedExpert.getActivityCareer()).isEqualTo("수정 후 경력");
        assertThat(updatedExpert.getActivityAreas()).isEqualTo(List.of("수정 후 지역", "새로운 지역"));

        // Project 검증
        List<ProjectEntity> updatedProjects = projectJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(updatedProjects).hasSize(2) // 2개에서 1개 추가 - 1개 삭제 , 1개 수정 = 2개
                .extracting(
                        ProjectEntity::getProjectName
                ).containsExactlyInAnyOrder("수정된 프로젝트명", "새로운 프로젝트명");

        // Skill 검증
        List<SkillEntity> updatedSkills = skillJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(updatedSkills).hasSize(2) // 2개에서 1개 추가 - 1개 삭제 , 1개 수정 = 2개
                .extracting(
                        SkillEntity::getSkillType,
                        SkillEntity::getContent
                ).containsExactlyInAnyOrder(
                        tuple(LIGHT, "수정된 내용"),
                        tuple(EDIT, "새로운 내용")
                );

        // Studio 검증
        Optional<StudioEntity> optionalUpdatedStudio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(optionalUpdatedStudio).isPresent();
        StudioEntity updateStudio = optionalUpdatedStudio.get();
        assertThat(updateStudio)
                .extracting(
                        StudioEntity::getStudioName,
                        StudioEntity::getEmployeesCount
                ).contains("수정된 이름", 10);
    }

    @Test
    @DisplayName("사용자는 전문가의 스튜디오 정보를 삭제할 수 있다..")
    public void updateExpertDetailInfo_delete_studio() throws Exception {
        // given
        // 테스트 데이터 셋팅
        UserEntity userEntity = givenUserEntity();
        userJpaRepository.save(userEntity);
        String userNo = userEntity.getUserNo();

        ExpertEntity expertEntity = givenExpertEntity("경력", List.of("지역"), userEntity);
        expertJpaRepository.save(expertEntity);
        String expertNo = expertEntity.getExpertNo();

        userEntity.assignExpertNo(expertNo);


        StudioEntity shdDelStudioEntity = givenStudioEntity(expertEntity,
                "삭제될 스튜디오명", 5, "10:00 - 19:00", "서울시 은평구");
        studioJpaRepository.save(shdDelStudioEntity);

        commitTestTransaction();

        // 요청 셋팅
        UpdateExpertDetailInfoRequest request = builder()
                .activityCareer("경력")
                .activityAreas(List.of("지역"))
                .studio(
                        StudioWebRequest.builder()
                                .studioNo(shdDelStudioEntity.getStudioNo())
                                .studioName(shdDelStudioEntity.getStudioName())
                                .employeesCount(shdDelStudioEntity.getEmployeesCount())
                                .businessHours(shdDelStudioEntity.getBusinessHours())
                                .address(shdDelStudioEntity.getAddress())
                                .changeStatus(DELETE)
                                .build()
                )
                .build();
        HttpEntity<UpdateExpertDetailInfoRequest> httpEntity = settingWebRequest(userEntity, request);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/detail_info",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Optional<StudioEntity> optionalUpdatedStudio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(optionalUpdatedStudio).isNotPresent();
    }

    @Test
    @DisplayName("사용자는 전문가의 스튜디오 정보를 추가할 수 있다..")
    public void updateExpertDetailInfo_add_studio() throws Exception {
        // given
        // 테스트 데이터 셋팅
        UserEntity userEntity = givenUserEntity();
        userJpaRepository.save(userEntity);
        String userNo = userEntity.getUserNo();

        ExpertEntity expertEntity = givenExpertEntity("경력", List.of("지역"), userEntity);
        expertJpaRepository.save(expertEntity);
        String expertNo = expertEntity.getExpertNo();
        userEntity.assignExpertNo(expertNo);
        commitTestTransaction();

        // 요청 셋팅
        UpdateExpertDetailInfoRequest request = builder()
                .activityCareer(expertEntity.getActivityCareer())
                .activityAreas(expertEntity.getActivityAreas())
                .studio(
                        StudioWebRequest.builder()
                                .studioName("새로운 스튜디오")
                                .employeesCount(10)
                                .businessHours("10:00~19:00")
                                .address("서울 강남구")
                                .changeStatus(NEW)
                                .build()
                )
                .build();
        HttpEntity<UpdateExpertDetailInfoRequest> httpEntity = settingWebRequest(userEntity, request);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/detail_info",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Optional<StudioEntity> optionalUpdatedStudio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(optionalUpdatedStudio).isPresent();
        assertThat(optionalUpdatedStudio.get())
                .extracting(
                        StudioEntity::getStudioName,
                        StudioEntity::getEmployeesCount,
                        StudioEntity::getBusinessHours,
                        StudioEntity::getAddress
                ).contains(
                        "새로운 스튜디오", 10, "10:00~19:00", "서울 강남구"
                );
    }


    private <T> HttpEntity<T> settingWebRequest(UserEntity userEntity, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
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

    private UserEntity givenUserEntity() {
        return UserEntity.builder()
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
                .expertNo(null)
                .build();
    }

    private ProfileImageEntity givenProfileImageEntity(String fileKey, String userNo) {
        return ProfileImageEntity.builder()
                .file_key(fileKey)
                .userNo(userNo)
                .build();
    }

    private ExpertEntity givenExpertEntity(String backgroundImageKey, String intro, String link, UserEntity userEntity) {
        return ExpertEntity.builder()
                .backgroundImageKey(backgroundImageKey)
                .intro(intro)
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(0)
                .lastActivityAt(LocalDateTime.of(2020, 10, 10, 1, 0))
                .portfolioLinks(List.of(link))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
    }

    private ExpertEntity givenExpertEntity(String activityCareer, List<String> activityAreas, UserEntity userEntity) {
        return ExpertEntity.builder()
                .backgroundImageKey("backgroundImageKey")
                .intro("intro")
                .activityCareer(activityCareer)
                .activityAreas(activityAreas)
                .activityCount(0)
                .lastActivityAt(LocalDateTime.of(2020, 10, 10, 1, 0))
                .portfolioLinks(List.of("link"))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
    }

    private ProjectEntity givenProjectEntity(ExpertEntity expertEntity, String projectName) {
        return ProjectEntity.builder()
                .expertEntity(expertEntity)
                .projectName(projectName)
                .startDate(LocalDateTime.of(2025, 5, 5, 10, 10))
                .endDate(LocalDateTime.of(2025, 5, 5, 10, 15))
                .build();
    }

    private SkillEntity givenSkillEntity(ExpertEntity expertEntity, SkillType skillType, String content) {
        return SkillEntity.builder()
                .expertEntity(expertEntity)
                .skillType(skillType)
                .content(content)
                .build();
    }

    private StudioEntity givenStudioEntity(ExpertEntity expertEntity, String studioName, int employeesCount, String businessHours, String address) {
        return StudioEntity.builder()
                .expertEntity(expertEntity)
                .studioName(studioName)
                .employeesCount(employeesCount)
                .businessHours(businessHours)
                .address(address)
                .build();
    }

    private UpdateExpertBasicInfoRequest givenWebRequest(String profileKey, String backgroundImageFileKey, String nickname, List<String> links, String intro) {
        return UpdateExpertBasicInfoRequest.builder()
                .profileImageFileKey(profileKey)
                .backgroundImageFileKey(backgroundImageFileKey)
                .nickname(nickname)
                .link(links)
                .intro(intro)
                .build();
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
