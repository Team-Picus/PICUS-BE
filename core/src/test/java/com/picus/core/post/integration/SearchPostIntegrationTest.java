package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.request.SearchPostRequest;
import com.picus.core.post.adapter.in.web.data.response.SearchPostResponse;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
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
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.post.domain.vo.PostMoodType.*;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static com.picus.core.post.domain.vo.PostThemeType.SNAP;
import static com.picus.core.post.domain.vo.SnapSubTheme.FAMILY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class SearchPostIntegrationTest {
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
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 특정 조건의 게시물을 검색할 수 있다.")
    public void search() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity();

        PostEntity p1 = createPostEntity(List.of("pkg-1"), userEntity.getExpertNo(), "가을 스냅 촬영",
                "가을 분위기의 한 줄 소개", "가을 배경의 실내 스냅 촬영 포스트입니다.",
                List.of(SNAP), List.of(FAMILY), List.of(COZY), SpaceType.INDOOR,
                "서울특별시 강남구", false
        );
        PostEntity p2 = createPostEntity(List.of("pkg-2"), userEntity.getExpertNo(), "여름 데일리샷",
                "시원한 여름 데일리샷", "강남 거리에서 찍은 데일리 사진",
                List.of(BEAUTY), List.of(), List.of(INTENSE), SpaceType.OUTDOOR,
                "서울특별시 강남구", false
        );
        PostEntity p3 = createPostEntity(List.of("pkg-3"), userEntity.getExpertNo(), "스냅 없는 글",
                "그냥 스냅은 없음", "이 게시글에는 스냅이 없습니다.",
                List.of(BEAUTY), List.of(), List.of(COZY), SpaceType.INDOOR,
                "서울특별시 강남구", false
        );
        commitTestTransaction();


        // given - 요청 셋팅
        SearchPostRequest request = createRequest(List.of(SNAP), List.of(), SpaceType.INDOOR,
                "서울특별시 강남구", List.of(COZY), "스냅");
        HttpEntity<Object> httpEntity = settingWebRequest(userEntity, null);

        // when  - API 요청
        URI uri = createUri(request, "/api/v1/posts/search/results");

        ResponseEntity<BaseResponse<SearchPostResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BaseResponse<SearchPostResponse> body = response.getBody();
        assertThat(body).isNotNull();

        SearchPostResponse result = body.getResult();

        assertThat(result.isLast()).isTrue();
        assertThat(result.posts()).hasSize(1)
                .extracting(
                        SearchPostResponse.PostResponse::postNo,
                        SearchPostResponse.PostResponse::thumbnailUrl,
                        SearchPostResponse.PostResponse::authorNickname,
                        SearchPostResponse.PostResponse::title
                ).containsExactlyInAnyOrder(
                        tuple(p1.getPostNo(), "", userEntity.getNickname(), p1.getTitle())
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
                .expertNo("expertNo")
                .build();
        return userJpaRepository.save(userEntity);
    }

    private PostEntity createPostEntity(List<String> packageNos, String expertNo, String title, String oneLineDescription,
                                        String detailedDescription, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned) {
        PostEntity postEntity = PostEntity.builder()
                .packageNos(packageNos)
                .expertNo(expertNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .snapSubThemes(snapSubThemes)
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

    private SearchPostRequest createRequest(List<PostThemeType> themeTypes, List<SnapSubTheme> snapSubThemes, SpaceType spaceType, String address, List<PostMoodType> moodTypes, String keyword) {
        return SearchPostRequest.builder()
                .themeTypes(themeTypes)
                .snapSubThemes(snapSubThemes)
                .spaceType(spaceType)
                .address(address)
                .moodTypes(moodTypes)
                .keyword(keyword)  // titleKeyword
                .build();
    }

    private <T> HttpEntity<T> settingWebRequest(UserEntity userEntity, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

    private URI createUri(SearchPostRequest request, String path) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path);

        if (!request.getThemeTypes().isEmpty()) {
            builder.queryParam("themeTypes", request.getThemeTypes().toArray());
        }
        if (!request.getSnapSubThemes().isEmpty()) {
            builder.queryParam("snapSubThemes", request.getSnapSubThemes().toArray());
        }
        if (request.getSpaceType() != null) {
            builder.queryParam("spaceType", request.getSpaceType());
        }
        if (StringUtils.hasText(request.getAddress())) {
            builder.queryParam("address", request.getAddress());
        }
        if (!request.getMoodTypes().isEmpty()) {
            builder.queryParam("moodTypes", request.getMoodTypes().toArray());
        }
        if (StringUtils.hasText(request.getKeyword())) {
            builder.queryParam("keyword", request.getKeyword());
        }
        if (request.getSortBy() != null) {
            builder.queryParam("sortBy", request.getSortBy());
        }
        if (request.getSortDirection() != null) {
            builder.queryParam("sortDirection", request.getSortDirection());
        }
        if (request.getCursor() != null) {
            builder.queryParam("cursor", request.getCursor());
        }
        if (request.getSize() <= 0) {
            builder.queryParam("size", request.getSize());
        }

        return builder.encode().build().toUri();
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
