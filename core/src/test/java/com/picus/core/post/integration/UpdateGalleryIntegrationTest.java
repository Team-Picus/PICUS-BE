package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.assertj.core.api.Assertions;
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
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class UpdateGalleryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PostJpaRepository postJpaRepository;

    @AfterEach
    void tearDown() {
        postJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();;
    }

    @Test
    @DisplayName("사용자는 자신(전문가)의 갤러리(고정처리 게시물)을 변경할 수 있다. 기존 고정처리된 게시물은 고정해제된다.")
    public void updateGallery() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        userEntity.assignExpertNo(userEntity.getUserNo());

        String expertNo = userEntity.getExpertNo(); // User와 Expert는 PK를 공유

        PostEntity postEntity1 = createPostEntity(
                List.of("package-123"), expertNo, "title1", "one1",
                "detail1", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "address1", true
        );
        PostEntity postEntity2 = createPostEntity(
                List.of("package-123"), expertNo, "title2", "one2",
                "detail2", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "address2", false
        );
        commitTestTransaction();

        // given - 응답값 셋팅
        HttpEntity<Object> httpEntity = settingWebRequest(userEntity, null);

        // when - API 요청
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/experts/posts/{post_no}/gallery",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity2.getPostNo()
        );

        // then - 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then - 고정여부 변경 검증
        PostEntity unPinPost = postJpaRepository.findById(postEntity1.getPostNo())
                .orElseThrow();
        assertThat(unPinPost.getIsPinned()).isFalse();
        PostEntity pinPost = postJpaRepository.findById(postEntity2.getPostNo())
                .orElseThrow();
        assertThat(pinPost.getIsPinned()).isTrue();
    }


    /**
     * private 메서드
     */
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
                .build();
        return userJpaRepository.save(userEntity);
    }
    private PostEntity createPostEntity(List<String> packageNos, String expertNo, String title, String oneLineDescription,
                                        String detailedDescription, List<PostThemeType> postThemeTypes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned) {
        PostEntity postEntity = PostEntity.builder()
                .packageNos(packageNos)
                .expertNo(expertNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .build();
        return postJpaRepository.save(postEntity);
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
