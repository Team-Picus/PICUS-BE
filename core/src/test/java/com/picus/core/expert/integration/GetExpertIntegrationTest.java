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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    @DisplayName("사용자는 전문가의 기본정보를 조회할 수 있다.")
    public void getExpertBasicInfo() throws Exception {
        // given
        ExpertEntity expertEntity = settingDefaultEntityData();
        String accessToken = tokenProvider.createAccessToken("test_id", "ROLE_USER");

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
        ExpertEntity expertEntity = settingDefaultEntityData();
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
        assertThat(result.activityAreas()).isEqualTo(List.of(ActivityArea.SEOUL_GANGBUKGU));

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
}
