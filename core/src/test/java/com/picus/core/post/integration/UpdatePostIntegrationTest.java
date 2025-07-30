package com.picus.core.post.integration;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.application.port.in.command.ChangeStatus;
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

import static com.picus.core.post.application.port.in.command.ChangeStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class UpdatePostIntegrationTest {
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
    @DisplayName("사용자는 게시물을 수정할 수 있다. 게시물이 수정되면 Expert의 최근활동일이 최신화 된다.")
    public void write_success() throws Exception {
        // given

        // 데이터베이스에 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        LocalDateTime initLastActivityAt = LocalDateTime.now().minusDays(1);
        ExpertEntity expertEntity = createExpertEntity(userEntity, initLastActivityAt);
        userEntity.assignExpertNo(expertEntity.getExpertNo());

        PostEntity postEntity = createPostEntity(
                "package-123", expertEntity.getExpertNo(), "old_title", "old_one",
                "old_detail", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "old_address", false
        );
        PostImageEntity postImageEntity1 = createPostImageEntity("file_key", 1, postEntity);
        PostImageEntity postImageEntity2 = createPostImageEntity("file_key", 2, postEntity);

        commitTestTransaction();

        // 입력 값 셋팅
        UpdatePostRequest webReq = createWebReq(List.of(
                        createPostImageWebReq(null, "new_file_key", 1, NEW), // 새로 추가된 이미지
                        createPostImageWebReq(postImageEntity1.getPostImageNo(), "file_key", 2, UPDATE), // 수정된 이미지
                        createPostImageWebReq(postImageEntity2.getPostImageNo(), null, null, DELETE) // 삭제된 이미지
                ), "new_title", "new_one", "new_detail",
                List.of(PostThemeType.BEAUTY, PostThemeType.EVENT), List.of(PostMoodType.VINTAGE),
                SpaceType.OUTDOOR, "new_address", "package-456");

        HttpEntity<UpdatePostRequest> httpEntity = settingWebRequest(userEntity, webReq);

        // when
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/posts/{post_no}",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity.getPostNo()
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // PostEntity 검증
        PostEntity postResult = postJpaRepository.findById(postEntity.getPostNo())
                .orElseThrow();
        assertThat(postResult.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(postResult.getPackageNo()).isEqualTo("package-456");
        assertThat(postResult.getExpertNo()).isEqualTo(expertEntity.getExpertNo());
        assertThat(postResult.getTitle()).isEqualTo("new_title");
        assertThat(postResult.getOneLineDescription()).isEqualTo("new_one");
        assertThat(postResult.getDetailedDescription()).isEqualTo("new_detail");
        assertThat(postResult.getPostThemeTypes()).isEqualTo(List.of(PostThemeType.BEAUTY, PostThemeType.EVENT));
        assertThat(postResult.getPostMoodTypes()).isEqualTo(List.of(PostMoodType.VINTAGE));
        assertThat(postResult.getSpaceType()).isEqualTo(SpaceType.OUTDOOR);
        assertThat(postResult.getSpaceAddress()).isEqualTo("new_address");

        // PostImageEntity 검증
        List<PostImageEntity> imageResults = postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo());
        assertThat(imageResults).hasSize(2)
                .extracting(
                        PostImageEntity::getFileKey,
                        PostImageEntity::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("new_file_key", 1),
                        tuple("file_key", 2)
                );

        ExpertEntity updatedExpertEntity = expertJpaRepository.findById(expertEntity.getExpertNo())
                .orElseThrow();
        assertThat(updatedExpertEntity.getLastActivityAt()).isAfter(initLastActivityAt);
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
                .expertNo("expert-123")
                .build();
        return userJpaRepository.save(userEntity);
    }
    private ExpertEntity createExpertEntity(UserEntity userEntity, LocalDateTime lastActivityAt) {
        ExpertEntity expertEntity = ExpertEntity.builder()
                .activityCareer("activityCareer")
                .activityAreas(List.of("activityAreas"))
                .intro("전문가 소개")
                .activityCount(0)
                .lastActivityAt(lastActivityAt)
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
        return expertJpaRepository.save(expertEntity);
    }

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

    private UpdatePostRequest createWebReq(
            List<UpdatePostRequest.PostImageRequest> postImages, String title, String oneLineDescription,
            String detailedDescription, List<PostThemeType> postThemeTypes, List<PostMoodType> postMoodTypes,
            SpaceType spaceType, String spaceAddress, String packageNo) {
        return UpdatePostRequest.builder()
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

    private UpdatePostRequest.PostImageRequest createPostImageWebReq(
            String postImageNo, String fileKey, Integer imageOrder, ChangeStatus changeStatus) {
        return UpdatePostRequest.PostImageRequest.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .changeStatus(changeStatus)
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
