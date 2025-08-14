package com.picus.core.post.integration;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;

public class DeletePostIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ExpertJpaRepository expertJpaRepository;
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private PostImageJpaRepository postImageJpaRepository;

    @AfterEach
    void tearDown() {
        postImageJpaRepository.deleteAllInBatch();
        postJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 게시물을 삭제할 때, 기존 게시물이 1개이고 그외에 게시물과 예약이 없으면 최근 활동일은 null이 된다.")
    public void delete_ifPostIsOne() throws Exception {

        // given
        int initActivityCount = 5;
        LocalDateTime initLastActivityAt = LocalDateTime.now().minusDays(1);

        // given - 데이터베이스에 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        ExpertEntity expertEntity = createExpertEntity(userEntity, initActivityCount, initLastActivityAt);
        userEntity.assignExpertNo(expertEntity.getExpertNo());

        PostEntity postEntity = createPostEntity(
                List.of("package-123"), expertEntity.getExpertNo(), "old_title", "old_one",
                "old_detail", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "old_address", false
        );
        createPostImageEntity("file_key", 1, postEntity);
        createPostImageEntity("file_key", 2, postEntity);
        commitTestTransaction();

        HttpEntity<Object> httpEntity = settingWebRequest(userEntity, null);

        // when - API 요청
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/posts/{post_no}",
                DELETE,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity.getPostNo()
        );

        // then
        // 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Post 삭제 검증
        assertThat(postJpaRepository.findById(postEntity.getPostNo()))
                .isNotPresent();

        // PostImage 삭제 검증
        assertThat(postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo()))
                .isEmpty();

        // Expert 정보 갱신 검증
        ExpertEntity updatedExpert = expertJpaRepository.findById(expertEntity.getExpertNo())
                .orElseThrow();
        assertThat(updatedExpert.getActivityCount()).isEqualTo(initActivityCount - 1);
        assertThat(updatedExpert.getLastActivityAt()).isNull(); // 1개 있었는데 삭제돼서 Null이됨
    }

    @Test
    @DisplayName("사용자가 게시물을 삭제할 때 글이 2개 이상이면 최근활동일을 나머지 게시물, 예약 중 최신 활동으로 갱신함")
    public void delete_ifPostIsMoreThanTwo() throws Exception {
        // given
        int initActivityCount = 5;
        LocalDateTime initLastActivityAt = LocalDateTime.now().minusDays(1);

        // given - 데이터베이스에 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        ExpertEntity expertEntity = createExpertEntity(userEntity, initActivityCount, initLastActivityAt);
        userEntity.assignExpertNo(expertEntity.getExpertNo());

        PostEntity postEntity1 = createPostEntity(
                List.of("package-123"), expertEntity.getExpertNo(), "old_title", "old_one",
                "old_detail", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "old_address", false
        );
        PostEntity postEntity2 = createPostEntity(
                List.of("package-123"), expertEntity.getExpertNo(), "old_title", "old_one",
                "old_detail", List.of(PostThemeType.BEAUTY), List.of(PostMoodType.COZY),
                SpaceType.INDOOR, "old_address", false
        );
        commitTestTransaction();

        HttpEntity<Object> httpEntity = settingWebRequest(userEntity, null);

        // when - API 요청
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/posts/{post_no}",
                DELETE,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                postEntity1.getPostNo()
        );

        // then
        // 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Post 삭제 검증
        assertThat(postJpaRepository.findById(postEntity1.getPostNo()))
                .isNotPresent();

        // Expert 정보 갱신 검증
        ExpertEntity updatedExpert = expertJpaRepository.findById(expertEntity.getExpertNo())
                .orElseThrow();
        assertThat(updatedExpert.getActivityCount()).isEqualTo(initActivityCount - 1);
        assertThat(updatedExpert.getLastActivityAt()).isEqualTo(postEntity2.getUpdatedAt()); // 1개 있었는데 삭제돼서 Null이됨
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
                .build();
        return userJpaRepository.save(userEntity);
    }

    private ExpertEntity createExpertEntity(UserEntity userEntity, int activityCount, LocalDateTime lastActivityAt) {
        ExpertEntity expertEntity = ExpertEntity.builder()
                .activityCareer("activityCareer")
                .activityAreas(List.of("activityAreas"))
                .intro("전문가 소개")
                .activityCount(activityCount)
                .lastActivityAt(lastActivityAt)
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
        return expertJpaRepository.save(expertEntity);
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

    private PostImageEntity createPostImageEntity(String fileKey, int imageOrder, PostEntity postEntity) {
        PostImageEntity postImageEntity = PostImageEntity.builder()
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .postEntity(postEntity)
                .build();
        return postImageJpaRepository.save(postImageEntity);
    }
}
