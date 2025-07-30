package com.picus.core.post.integration;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest.PostImageWebReq;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class CreatePostIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    PostJpaRepository postJpaRepository;
    @Autowired
    private PostImageJpaRepository postImageJpaRepository;
    @Autowired
    private ExpertJpaRepository expertJpaRepository;

    @AfterEach
    void tearDown() {
        postImageJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 게시물을 작성할 수 있다.")
    public void write_success() throws Exception {
        // given

        // 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        int initActivityCount = 8;
        LocalDateTime initLastActivityAt = LocalDateTime.now().minusDays(1);
        ExpertEntity expertEntity = createExpertEntity(userEntity, initActivityCount, initLastActivityAt);
        userEntity.assignExpertNo(expertEntity.getExpertNo());
        commitTestTransaction();

        // 입력값 셋팅
        CreatePostRequest webReq = createWebReq(
                List.of(
                        CreatePostRequest.PostImageWebReq.builder().fileKey("img1.jpg").imageOrder(1).build(),
                        CreatePostRequest.PostImageWebReq.builder().fileKey("img2.jpg").imageOrder(2).build()
                ),
                "테스트 제목",
                "한 줄 설명",
                "자세한 설명",
                List.of(PostThemeType.BEAUTY),
                List.of(PostMoodType.COZY),
                SpaceType.INDOOR,
                "서울시 강남구",
                "pkg-001"
        );

        // 요청 셋팅
        HttpEntity<CreatePostRequest> httpEntity = settingWebRequest(userEntity, webReq);

        // when - API 요청
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/posts",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Post 저장 검증
        List<PostEntity> postEntities = postJpaRepository.findAll();
        assertThat(postEntities).hasSize(1)
                .extracting(
                        "packageNo", "title", "oneLineDescription", "detailedDescription",
                        "postThemeTypes", "postMoodTypes", "spaceType", "spaceAddress", "isPinned"
                )
                .containsExactly(
                        tuple(
                                "pkg-001",
                                "테스트 제목",
                                "한 줄 설명",
                                "자세한 설명",
                                List.of(PostThemeType.BEAUTY),
                                List.of(PostMoodType.COZY),
                                SpaceType.INDOOR,
                                "서울시 강남구",
                                false
                        )
                );

        // PostImage 저장 검증
        List<PostImageEntity> postImageEntities = postImageJpaRepository.findAll();
        assertThat(postImageEntities).hasSize(2)
                .extracting("fileKey", "imageOrder")
                .containsExactlyInAnyOrder(
                        tuple("img1.jpg", 1),
                        tuple("img2.jpg", 2)
                );

        // Expert 정보 업데이트 검증
        ExpertEntity updatedExpert = expertJpaRepository.findById(expertEntity.getExpertNo())
                .orElseThrow();
        assertThat(updatedExpert.getActivityCount()).isEqualTo(initActivityCount + 1);
        assertThat(updatedExpert.getLastActivityAt()).isAfter(initLastActivityAt);
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .name("이름")
                .nickname("nickname")
                .tel("01012345678")
                .role(Role.EXPERT)
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
    private ExpertEntity createExpertEntity(UserEntity userEntity, int activityCount, LocalDateTime lastActivityAt) {
        ExpertEntity expertEntity = ExpertEntity.builder()
                .activityCareer("activityCareer")
                .activityAreas(List.of("activityAreas"))
                .intro("전문가 소개")
                .activityCount(activityCount)
                .lastActivityAt(lastActivityAt)
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
        return expertJpaRepository.save(expertEntity);
    }

    private CreatePostRequest createWebReq(
            List<PostImageWebReq> postImages,
            String title,
            String oneLineDescription,
            String detailedDescription,
            List<PostThemeType> postThemeTypes,
            List<PostMoodType> postMoodTypes,
            SpaceType spaceType,
            String spaceAddress,
            String packageNo
    ) {
        return CreatePostRequest.builder()
                .postImages(postImages)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .packageNo(packageNo)
                .build();
    }

    private <T> HttpEntity<T> settingWebRequest(UserEntity userEntity, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
