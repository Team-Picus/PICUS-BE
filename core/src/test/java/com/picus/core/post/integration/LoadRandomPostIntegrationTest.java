package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.response.LoadRandomPostResponse;
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
public class LoadRandomPostIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private PostImageJpaRepository postImageJpaRepository;

    @Test
    @DisplayName("사용자는 랜덤으로 선정된 N개의 게시물을 조회할 수 있다.")
    public void load() throws Exception {
        // given
        int size = 2;
        // 데이터베이스 데이터 셋팅
        String expertNo = "expert-123";

        UserEntity userEntity = createUserEntity(expertNo);

        PostEntity postEntity1 = createPostEntity("t1", expertNo);
        PostImageEntity postImgEntity1 = createPostImageEntity("f1", 1, postEntity1);

        PostEntity postEntity2 = createPostEntity("t2", expertNo);
        PostImageEntity postImgEntity2 = createPostImageEntity("f2", 1, postEntity2);

        PostEntity postEntity3 = createPostEntity("t3", expertNo);
        PostImageEntity postImgEntity3 = createPostImageEntity("f3", 1, postEntity3);

        PostEntity postEntity4 = createPostEntity("t4", expertNo);
        PostImageEntity postImgEntity4 = createPostImageEntity("f4", 1, postEntity4);

        PostEntity postEntity5 = createPostEntity("t5", expertNo);
        PostImageEntity postImgEntity5 = createPostImageEntity("f5", 1, postEntity5);
        commitTestTransaction();

        HttpEntity<Object> httpEntity = settingWebRequest(userEntity, null);

        // when
        ResponseEntity<BaseResponse<List<LoadRandomPostResponse>>> response = restTemplate.exchange(
                "/api/v1/posts/random?size={size}",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                size
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BaseResponse<List<LoadRandomPostResponse>> body = response.getBody();
        assertThat(body).isNotNull();

        List<LoadRandomPostResponse> result = body.getResult();
        assertThat(result).hasSize(size);
    }


    private UserEntity createUserEntity(String expertNo) {
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
                .expertNo(expertNo)
                .build();
        return userJpaRepository.save(userEntity);
    }

    private PostEntity createPostEntity(String title, String expertNo) {
        PostEntity postEntity = PostEntity.builder()
                .packageNo("packageNo")
                .expertNo(expertNo)
                .title(title)
                .oneLineDescription("oneLineDescription")
                .detailedDescription("detailedDescription")
                .postThemeTypes(List.of(PostThemeType.BEAUTY))
                .postMoodTypes(List.of(PostMoodType.VINTAGE))
                .spaceType(SpaceType.OUTDOOR)
                .spaceAddress("spaceAddress")
                .isPinned(false)
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
