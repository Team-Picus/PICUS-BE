package com.picus.core.post.integration;


import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.request.CreateCommentRequest;
import com.picus.core.post.adapter.out.persistence.entity.CommentEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.repository.CommentJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
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

import static com.picus.core.post.domain.vo.PostMoodType.VINTAGE;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class CreateCommentIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @AfterEach
    void tearDown() {
        commentJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 댓글을 작성할 수 있다.")
    public void createComment() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        PostEntity postEntity = createPostEntity();
        commitTestTransaction();

        // given - 요청 셋팅
        CreateCommentRequest request = CreateCommentRequest.builder()
                .content("content")
                .build();
        HttpEntity<CreateCommentRequest> httpEntity = settingWebRequest(userEntity, request);


        // when - API 요청
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/posts/{post_no}/comments",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity.getPostNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Comment 저장 검증
        List<CommentEntity> commentEntities = commentJpaRepository.findAll();
        assertThat(commentEntities).hasSize(1)
                .extracting(
                        CommentEntity::getUserNo,
                        CommentEntity::getContent
                ).containsExactlyInAnyOrder(
                        tuple(userEntity.getUserNo(), request.content())
                );
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

    private PostEntity createPostEntity() {
        PostEntity postEntity = PostEntity.builder()
                .packageNos(List.of("packages"))
                .expertNo("expertNo")
                .title("title")
                .oneLineDescription("oneLineDescription")
                .detailedDescription("detailedDescription")
                .postThemeTypes(List.of(BEAUTY))
                .snapSubThemes(List.of())
                .postMoodTypes(List.of(VINTAGE))
                .spaceType(SpaceType.OUTDOOR)
                .spaceAddress("spaceAddress")
                .isPinned(false)
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
