package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq;
import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq.PostImageWebReq;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class WritePostIntegrationTest {
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

    @AfterEach
    void tearDown() {
        postImageJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 게시물을 작성할 수 있다.")
    public void write_success() throws Exception {
        // given
        UserEntity userEntity = createUserEntity();
        commitTestTransaction();

        WritePostWebReq webReq = createWebReq(
                List.of(
                        WritePostWebReq.PostImageWebReq.builder().fileKey("img1.jpg").imageOrder(1).build(),
                        WritePostWebReq.PostImageWebReq.builder().fileKey("img2.jpg").imageOrder(2).build()
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

        HttpEntity<WritePostWebReq> httpEntity = settingWebRequest(userEntity.getUserNo(), webReq);

        // when
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/posts",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

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

        List<PostImageEntity> postImageEntities = postImageJpaRepository.findAll();
        assertThat(postImageEntities).hasSize(2)
                .extracting("fileKey", "imageOrder")
                .containsExactlyInAnyOrder(
                        tuple("img1.jpg", 1),
                        tuple("img2.jpg", 2)
                );
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

    private WritePostWebReq createWebReq(
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
        return WritePostWebReq.builder()
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

    private <T> HttpEntity<T> settingWebRequest(String userNo, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userNo, "ROLE_USER");
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
