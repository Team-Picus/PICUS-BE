package com.picus.core.post.integration;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostResponse;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.IntegrationTestSupport;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SuggestPostIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 제목에 특정 키워드가 포함된 게시물을 N개 추천받을 수 있다.")
    public void suggestPosts() throws Exception {
        // given
        String keyword = "데";
        int size = 5;

        // 데이터베이스에 데이터 셋팅
        PostEntity postEntity1 = createPostEntity("안녕");
        PostEntity postEntity2 = createPostEntity("데일리 모먼트");
        PostEntity postEntity3 = createPostEntity("데일리");
        PostEntity postEntity4 = createPostEntity("일상이 영화가 되는 데일리 시네마");
        PostEntity postEntity5 = createPostEntity("그날의 데자뷰, 익숙하지만 새로운 순간들");
        PostEntity postEntity6 = createPostEntity("데일리씬");
        PostEntity postEntity7 = createPostEntity("하세요");
        PostEntity postEntity8 = createPostEntity("지갑");
        postJpaRepository.saveAll(List.of(postEntity1, postEntity2, postEntity3, postEntity4, postEntity5, postEntity6, postEntity7, postEntity8));
        commitTestTransaction();

        HttpEntity<Void> request = settingWebRequest(createUserEntity(), null);

        // when
        ResponseEntity<BaseResponse<List<SuggestPostResponse>>> response = restTemplate.exchange(
                "/api/v1/posts/search/suggestions?keyword={keyword}&size={size}",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                keyword, size
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BaseResponse<List<SuggestPostResponse>> body = response.getBody();
        assertThat(body).isNotNull();

        List<SuggestPostResponse> result = body.getResult();
        assertThat(result).hasSize(5)
                .extracting(SuggestPostResponse::title)
                .containsExactly(
                        "데일리",
                        "데일리 모먼트",
                        "데일리씬",
                        "그날의 데자뷰, 익숙하지만 새로운 순간들",
                        "일상이 영화가 되는 데일리 시네마"
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

    private PostEntity createPostEntity(String title) {
        PostEntity postEntity = PostEntity.builder()
                .packageNos(List.of("packages"))
                .expertNo("expertNo")
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
}
