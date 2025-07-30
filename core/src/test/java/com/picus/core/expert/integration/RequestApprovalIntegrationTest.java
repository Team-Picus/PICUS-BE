package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.entity.SkillEntity;
import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.ProjectJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.SkillJpaRepository;
import com.picus.core.expert.adapter.out.persistence.repository.StudioJpaRepository;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class RequestApprovalIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
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
        projectJpaRepository.deleteAllInBatch();
        skillJpaRepository.deleteAllInBatch();
        studioJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 전문가 승인요청을 보낼 수 있다.")
    public void requestApproval() throws Exception {
        // given
        UserEntity userEntity = settingTestUserEntityData();
        commitTestTransaction();

        String expertNo = userEntity.getUserNo(); // UserEntity의 PK와 ExpertEntity의 PK는 같음
        String accessToken = tokenProvider.createAccessToken(expertNo, userEntity.getRole().toString());
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<RequestApprovalWebRequest> httpEntity = new HttpEntity<>(webRequest, headers);

        // when
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/experts/approval-requests",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then - Response
        assertResponse(response);
        // then - Expert
        assertExpert(expertNo);
        // then - Projects
        assertProjects(expertNo);
        // then - Skills
        assertSkills(expertNo);
        // then - Studio
        assertStudio(expertNo);
    }

    private void assertResponse(ResponseEntity<BaseResponse<Void>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void assertExpert(String expertNo) {
        Optional<ExpertEntity> optionalSavedExpertEntity = expertJpaRepository.findById(expertNo);
        assertThat(optionalSavedExpertEntity).isPresent();
        ExpertEntity savedExpertEntity = optionalSavedExpertEntity.get();
        assertThat(savedExpertEntity.getActivityCareer()).isEqualTo("3년차");
        assertThat(savedExpertEntity.getActivityAreas()).containsExactlyInAnyOrder(
                "서울 강북구",
                "서울 강동구"
        );
        assertThat(savedExpertEntity.getPortfolioLinks()).hasSize(2)
                .containsExactlyInAnyOrder(
                        "https://myportfolio.com/project1",
                        "https://myportfolio.com/project2"
                );
    }

    private void assertProjects(String expertNo) {
        List<ProjectEntity> projects = projectJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(projects).hasSize(2)
                .extracting("projectName", "startDate", "endDate")
                .containsExactlyInAnyOrder(
                        tuple("단편영화 촬영 프로젝트",
                                LocalDateTime.of(2022, 5, 1, 0, 0),
                                LocalDateTime.of(2022, 8, 15, 0, 0)),
                        tuple("뮤직비디오 조명 작업",
                                LocalDateTime.of(2023, 1, 10, 0, 0),
                                LocalDateTime.of(2023, 2, 20, 0, 0))
                );
    }

    private void assertSkills(String expertNo) {
        List<SkillEntity> skills = skillJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(skills).hasSize(2)
                .extracting("skillType", "content")
                .containsExactlyInAnyOrder(
                        tuple(SkillType.CAMERA, "시네마 카메라 운용 가능 (RED, Blackmagic)"),
                        tuple(SkillType.EDIT, "프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                );
    }

    private void assertStudio(String expertNo) {
        Optional<StudioEntity> studio = studioJpaRepository.findByExpertEntity_ExpertNo(expertNo);
        assertThat(studio).isPresent();
        assertThat(studio.get())
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

    private RequestApprovalWebRequest givenRequestApprovalWebRequest() {
        return new RequestApprovalWebRequest(
                "3년차",
                List.of("서울 강북구", "서울 강동구"),
                List.of(
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build(),
                        RequestApprovalWebRequest.ProjectWebRequest.builder()
                                .projectName("뮤직비디오 조명 작업")
                                .startDate(LocalDateTime.of(2023, 1, 10, 0, 0))
                                .endDate(LocalDateTime.of(2023, 2, 20, 0, 0))
                                .build()
                ),
                List.of(
                        RequestApprovalWebRequest.SkillWebRequest.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build(),
                        RequestApprovalWebRequest.SkillWebRequest.builder()
                                .skillType(SkillType.EDIT)
                                .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                                .build()
                ),
                RequestApprovalWebRequest.StudioWebRequest.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(),
                List.of(
                        "https://myportfolio.com/project1",
                        "https://myportfolio.com/project2"
                )
        );
    }
    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
