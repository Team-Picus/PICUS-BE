package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.response.LoadCommentByPostResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.post.domain.vo.PostMoodType.VINTAGE;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class LoadCommentByPostIntegrationTest {
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
    @DisplayName("사용자는 특정 게시물의 댓글들을 조회할 수 있다.")
    public void load() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity("nick1");
        PostEntity postEntity = createPostEntity();
        LocalDateTime createdAt = LocalDateTime.of(2000, 1, 1, 1, 1);
        CommentEntity cmt1 = createCommentEntity(postEntity, userEntity.getUserNo(), "content1", createdAt);
        CommentEntity cmt2 = createCommentEntity(postEntity, userEntity.getUserNo(), "content2", createdAt);
        commitTestTransaction();

        // given - 요청 셋팅
        HttpEntity<Object> httpEntity = settingWebRequest(userEntity, null);

        // when
        ResponseEntity<BaseResponse<LoadCommentByPostResponse>> response = restTemplate.exchange(
                "/api/v1/posts/{post_no}/comments",
                GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity.getPostNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<LoadCommentByPostResponse> body = response.getBody();
        assertThat(body).isNotNull();

        LoadCommentByPostResponse result = body.getResult();
        assertThat(result.comments())
                .extracting(
                        LoadCommentByPostResponse.CommentResponse::commentNo,
                        LoadCommentByPostResponse.CommentResponse::authorNo,
                        LoadCommentByPostResponse.CommentResponse::authorNickname,
                        LoadCommentByPostResponse.CommentResponse::authorProfileImageUrl,
                        LoadCommentByPostResponse.CommentResponse::content,
                        LoadCommentByPostResponse.CommentResponse::createdAt
                )
                .containsExactlyInAnyOrder(
                        tuple(cmt1.getCommentNo(), cmt1.getUserNo(), userEntity.getNickname(),
                                "", cmt1.getContent(), cmt1.getCreatedAt()),
                        tuple(cmt2.getCommentNo(), cmt2.getUserNo(), userEntity.getNickname(),
                                "", cmt2.getContent(), cmt2.getCreatedAt())
                ); // TODO: filekey -> url 변환 로직
    }

    private UserEntity createUserEntity(String nickname) {
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
                .expertNo("expertNo")
                .build();
        return userJpaRepository.save(userEntity);
    }
    private PostEntity createPostEntity() {
        PostEntity postEntity = PostEntity.builder()
                .packageNo("packageNo")
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
    private CommentEntity createCommentEntity(PostEntity postEntity, String userNo, String content, LocalDateTime createdAt) {
        CommentEntity commentEntity = CommentEntity.builder()
                .postEntity(postEntity)
                .userNo(userNo)
                .content(content)
                .createdAt(createdAt)
                .build();
        return commentJpaRepository.save(commentEntity);
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
