package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.response.LoadPostDetailResponse;
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
public class LoadPostDetailIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PostImageJpaRepository postImageJpaRepository;
    @Autowired
    private PostJpaRepository postJpaRepository;

    @AfterEach
    void tearDown() {
        postImageJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 특정 게시물을 조회할 수 있다.")
    public void load() throws Exception {
        // given
        // 데이터베이스에 데이터 셋팅
        String nickname = "nickname";
        String expertNo = "expert-456";
        PostEntity postEntity = createPostEntity(
                List.of("package-123"), expertNo, "제목", "설명",
                "상세 설명", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "서울시 강남구", true
        );
        PostImageEntity postImageEntity = createPostImageEntity("file.jpg", 1, postEntity);
        commitTestTransaction();

        HttpEntity<Void> httpEntity = settingWebRequest(createUserEntity(nickname, expertNo), null);
        // 웹 요청 셋팅

        // when
        ResponseEntity<BaseResponse<LoadPostDetailResponse>> response = restTemplate.exchange(
                "/api/v1/posts/{postNo}",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity.getPostNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<LoadPostDetailResponse> body = response.getBody();
        assertThat(body).isNotNull();

        LoadPostDetailResponse result = body.getResult();
        assertThat(result.postNo()).isEqualTo(postEntity.getPostNo());
        assertThat(result.title()).isEqualTo(postEntity.getTitle());
        assertThat(result.oneLineDescription()).isEqualTo(postEntity.getOneLineDescription());
        assertThat(result.detailedDescription()).isEqualTo(postEntity.getDetailedDescription());
        assertThat(result.themeTypes()).containsExactlyElementsOf(postEntity.getPostThemeTypes());
        assertThat(result.moodTypes()).containsExactlyElementsOf(postEntity.getPostMoodTypes());
        assertThat(result.spaceType()).isEqualTo(postEntity.getSpaceType());
        assertThat(result.spaceAddress()).isEqualTo(postEntity.getSpaceAddress());
        assertThat(result.packageNos()).isEqualTo(postEntity.getPackageNos());
        assertThat(result.updatedAt()).isNotNull();

        assertThat(result.authorInfo()).isNotNull();
        assertThat(result.authorInfo().expertNo()).isEqualTo(postEntity.getExpertNo());
        assertThat(result.authorInfo().nickname()).isEqualTo(nickname);

        assertThat(result.images())
                .hasSize(1)
                .extracting("imageNo", "fileKey", "imageUrl", "imageOrder")
                .containsExactlyInAnyOrder(
                        tuple(postImageEntity.getPostImageNo(), postImageEntity.getFileKey(), "", postImageEntity.getImageOrder())
                ); // TODO: fileKey -> url 변환 로직 추가후 재 검증
    }

    /**
     * private 메서드
     */
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deletedAt(LocalDateTime.now())
                .build();
        return postJpaRepository.save(postEntity);
    }

    private PostImageEntity createPostImageEntity(String fileKey, int imageOrder, PostEntity postEntity) {
        PostImageEntity postImageEntity = PostImageEntity.builder()
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .postEntity(postEntity)
                .build();
        return postImageJpaRepository.save(postImageEntity);
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }

    private UserEntity createUserEntity(String nickname, String expertNo) {
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
                .expertNo(expertNo)
                .build();
        return userJpaRepository.save(userEntity);
    }

    private <T> HttpEntity<T> settingWebRequest(UserEntity userEntity, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

}
