package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.common.BaseResponse;
import org.assertj.core.api.Assertions;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RequestApprovalIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private LoadExpertPort loadExpertPort;

    @Test
    @DisplayName("사용자는 전문가 승인요청을 보낼 수 있다.")
    public void requestApproval() throws Exception {
        // given
        String accessToken = tokenProvider.createAccessToken("test_id", "ROLE_USER");
        RequestApprovalWebRequest webRequest = givenRequestApprovalWebRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<RequestApprovalWebRequest> httpEntity = new HttpEntity<>(webRequest, headers);

        // when
        ResponseEntity<BaseResponse<RequestApprovalWebResponse>> response = restTemplate.exchange(
                "/api/v1/experts/approval-requests",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<RequestApprovalWebResponse> body = response.getBody();
        assertThat(body).isNotNull();

        RequestApprovalWebResponse result = body.getResult();
        Optional<Expert> findExpert = loadExpertPort.loadExpertByExpertNo(result.expertNo());

        assertThat(findExpert).isPresent();
        Expert expert = findExpert.get();

        assertThat(expert.getActivityCareer()).isEqualTo("3년차");
        assertThat(expert.getActivityAreas()).containsExactlyInAnyOrder(
                ActivityArea.SEOUL_GANGBUKGU,
                ActivityArea.SEOUL_GANGDONGGU
        );
        // Projects
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

        // Skills
        assertThat(expert.getSkills()).hasSize(2)
                .extracting("skillType", "content")
                .containsExactlyInAnyOrder(
                        tuple(SkillType.CAMERA, "시네마 카메라 운용 가능 (RED, Blackmagic)"),
                        tuple(SkillType.EDIT, "프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                );

        // Studio
        assertThat(expert.getStudio()).isNotNull();
        assertThat(expert.getStudio())
                .extracting("studioName", "employeesCount", "businessHours", "address")
                .containsExactly("크리에이티브 필름", 5, "10:00 - 19:00", "서울특별시 마포구 월드컵북로 400");

        // Portfolios
        assertThat(expert.getPortfolios()).hasSize(2)
                .extracting("link")
                .containsExactlyInAnyOrder(
                        "https://myportfolio.com/project1",
                        "https://myportfolio.com/project2"
                );
    }

    private RequestApprovalWebRequest givenRequestApprovalWebRequest() {
        return new RequestApprovalWebRequest(
                "3년차",
                List.of(ActivityArea.SEOUL_GANGBUKGU, ActivityArea.SEOUL_GANGDONGGU),
                List.of(
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
                ),
                List.of(
                        Skill.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build(),
                        Skill.builder()
                                .skillType(SkillType.EDIT)
                                .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                                .build()
                ),
                Studio.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build(),
                List.of(
                        Portfolio.builder()
                                .link("https://myportfolio.com/project1")
                                .build(),
                        Portfolio.builder()
                                .link("https://myportfolio.com/project2")
                                .build()
                )
        );
    }
}
