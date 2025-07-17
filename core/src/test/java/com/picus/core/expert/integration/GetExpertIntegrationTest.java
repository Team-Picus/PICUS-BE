package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class GetExpertIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;

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
    private EntityManager entityManager;

    @AfterTransaction
    void tearDown() {
        projectJpaRepository.deleteAllInBatch();
        skillJpaRepository.deleteAllInBatch();
        studioJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 전문가의 기본정보를 조회할 수 있다.")
    public void getExpertBasicInfo() throws Exception {
        // given
        UserEntity userEntity = settingTestUserEntityData();
        ExpertEntity expertEntity = settingTestExpertEntityData(userEntity);

        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // when
        ResponseEntity<BaseResponse<GetExpertBasicInfoWebResponse>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/basic_info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                expertEntity.getExpertNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<GetExpertBasicInfoWebResponse> body = response.getBody();
        assertThat(body).isNotNull();

        GetExpertBasicInfoWebResponse result = body.getResult();


        assertThat(result.activityDuration()).isNotNull();
        assertThat(result.activityCount()).isEqualTo(8);
        assertThat(result.lastActivityAt()).isEqualTo(LocalDateTime.of(2024, 5, 20, 10, 30));
        assertThat(result.intro()).isEqualTo("전문가 소개");
        // TODO: backgroundImageUrl 검증
    }

    @Test
    @DisplayName("사용자는 전문가의 상세정보를 조회할 수 있다.")
    public void getExpertDetailInfo() throws Exception {
        // given
        UserEntity userEntity = settingTestUserEntityData();
        ExpertEntity expertEntity = settingTestExpertEntityData(userEntity);
        String accessToken = tokenProvider.createAccessToken("test_id", "ROLE_USER");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // when
        ResponseEntity<BaseResponse<GetExpertDetailInfoWebResponse>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/detail_info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                expertEntity.getExpertNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<GetExpertDetailInfoWebResponse> body = response.getBody();
        assertThat(body).isNotNull();

        GetExpertDetailInfoWebResponse result = body.getResult();

        assertThat(result.activityCareer()).isEqualTo("경력 5년");
        assertThat(result.activityAreas()).isEqualTo(List.of("서울 강북구"));

        // Projects
        assertThat(result.projects()).hasSize(2)
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
        assertThat(result.skills()).hasSize(2)
                .extracting("skillType", "content")
                .containsExactlyInAnyOrder(
                        tuple(SkillType.CAMERA, "시네마 카메라 운용 가능 (RED, Blackmagic)"),
                        tuple(SkillType.EDIT, "프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                );

        // Studio
        assertThat(result.studio()).isNotNull();
        assertThat(result.studio())
                .extracting("studioName", "employeesCount", "businessHours", "address")
                .containsExactly("크리에이티브 필름", 5, "10:00 - 19:00", "서울특별시 마포구 월드컵북로 400");


    }

    private UserEntity settingTestUserEntityData() {
        UserEntity userEntity = UserEntity.builder()
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
        return userJpaRepository.save(userEntity);
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
        entityManager.flush();
        // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.flagForCommit();  // 커밋하도록 표시
        TestTransaction.end();            // 실제 커밋 수행

        // 새로운 트랜잭션 시작 (다시 @Transactional 범위로 돌아가기 위해)
        TestTransaction.start();

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
}
