package com.picus.core.post.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class LoadGalleryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private PostImageJpaRepository postImageJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void tearDown() {
        postImageJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 특정 전문가의 갤러리를 조회한다 - 해당 전문가의 갤러리 설정이 된 경우 해당 글 정보가 조회된다.")
    public void load_ifGallerySet() throws Exception {
        // given
        // 데이터베이스에 데이터 셋팅
        String expertNo = "expert-456";
        PostEntity postEntity = createPostEntity(
                "package-123", expertNo, "제목", "설명",
                "상세 설명", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "서울시 강남구", true
        );
        createPostImageEntity("file.jpg", 1, postEntity);
        commitTestTransaction();

        // 웹 요청 셋팅
        HttpEntity<Void> httpEntity = settingWebRequest(createUserEntity(), null);

        // when
        ResponseEntity<BaseResponse<Object>> response = restTemplate.exchange(
                "/api/v1/experts/posts/{expert_no}/gallery",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                expertNo
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<Object> body = response.getBody();
        assertThat(body).isNotNull();

        Map<String, String> result = (Map<String, String>) body.getResult();

        assertThat(result.get("postNo")).isEqualTo(postEntity.getPostNo());
        assertThat(result.get("thumbnailUrl")).isEqualTo(""); // TODO: fileKey -> url 변환 로직 추가후 재 검증
        assertThat(result.get("title")).isEqualTo(postEntity.getTitle());
        assertThat(result.get("oneLineDescription")).isEqualTo(postEntity.getOneLineDescription());
    }

    @Test
    @DisplayName("사용자는 특정 전문가의 갤러리를 조회할 수 있다. - 해당 전문가의 갤러리 설정이 안된 경우, 설정이 안됐다는 메시지를 반환한다.")
    public void load_ifGalleryNotSet() throws Exception {
        // given
        String expertNo = "expert-456";

        // 웹 요청 셋팅
        HttpEntity<Void> httpEntity = settingWebRequest(createUserEntity(), null);

        // when
        ResponseEntity<BaseResponse<Object>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/gallery",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                expertNo
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<Object> body = response.getBody();
        assertThat(body).isNotNull();

        String result = (String) body.getResult();
        assertThat(result).isEqualTo("아직 갤러리가 설정되지 않았습니다.");
    }

    /**
     * private 메서드
     */
    private PostEntity createPostEntity(String packageNo, String expertNo, String title, String oneLineDescription,
                                        String detailedDescription, List<PostThemeType> postThemeTypes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned) {
        PostEntity postEntity = PostEntity.builder()
                .packageNo(packageNo)
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
                .expertNo(null)
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
