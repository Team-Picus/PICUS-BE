package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.request.UpdateExpertBasicInfoWebRequest;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class UpdateExpertInfoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProfileImageJpaRepository profileImageJpaRepository;
    @Autowired
    private ExpertJpaRepository expertJpaRepository;

    @Test
    @DisplayName("사용자는 전문가의 기본정보를 수정할 수 있다.")
    public void updateExpertBasicInfo() throws Exception {
        // given
        // 테스트 데이터 셋팅
        UserEntity userEntity = givenUserEntity("old_nick");
        userJpaRepository.save(userEntity);
        String userNo = userEntity.getUserNo();

        ProfileImageEntity profileImageEntity = givenProfileImageEntity("old_profile_key", userNo);
        profileImageJpaRepository.save(profileImageEntity);
        String profileImageNo = profileImageEntity.getProfileImageNo();

        ExpertEntity expertEntity = givenExpertEntity("old_back_key", "old_intro", "old_link", userEntity);
        expertJpaRepository.save(expertEntity);
        String expertNo = expertEntity.getExpertNo();
        userEntity.assignExpertNo(expertNo);

        commitTestTransaction();

        // 요청 셋팅
        HttpEntity<UpdateExpertBasicInfoWebRequest> request = settingWebRequest(userNo, givenWebRequest(
                "new_profile_key",
                "new_back_key",
                "new_nick",
                "new_link",
                "new_intro"));

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/basic_info",
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 닉네임 변경 검증
        Optional<UserEntity> optionalUserResult = userJpaRepository.findById(userNo);
        assertThat(optionalUserResult).isPresent();
        UserEntity userResult = optionalUserResult.get();
        assertThat(userResult.getNickname()).isEqualTo("new_nick");

        // 프로필 이미지 파일키 변경 검증
        Optional<ProfileImageEntity> optionalProfileResult = profileImageJpaRepository.findById(profileImageNo);
        assertThat(optionalProfileResult).isPresent();
        ProfileImageEntity profileResult = optionalProfileResult.get();
        assertThat(profileResult.getFile_key()).isEqualTo("new_profile_key");

        // 전문가 정보 검증
        Optional<ExpertEntity> optionalExpertResult = expertJpaRepository.findById(expertNo);
        assertThat(optionalExpertResult).isPresent();
        ExpertEntity expertResult = optionalExpertResult.get();
        assertThat(expertResult).extracting(
                ExpertEntity::getBackgroundImageKey,
                ExpertEntity::getPortfolioLinks,
                ExpertEntity::getIntro
        ).containsExactly("new_back_key", List.of("old_link", "new_link"), "new_intro");
    }

    private HttpEntity<UpdateExpertBasicInfoWebRequest> settingWebRequest(String userNo, UpdateExpertBasicInfoWebRequest webRequest) {
        String accessToken = tokenProvider.createAccessToken(userNo, "ROLE_USER");
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

    private UserEntity givenUserEntity(String nickname) {
        return UserEntity.builder()
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
                .expertNo(null)
                .build();
    }

    private ProfileImageEntity givenProfileImageEntity(String fileKey, String userNo) {
        return ProfileImageEntity.builder()
                .file_key(fileKey)
                .userNo(userNo)
                .build();
    }

    private ExpertEntity givenExpertEntity(String backgroundImageKey, String intro, String link, UserEntity userEntity) {
        return ExpertEntity.builder()
                .backgroundImageKey(backgroundImageKey)
                .intro(intro)
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(0)
                .lastActivityAt(LocalDateTime.of(2020, 10, 10, 1, 0))
                .portfolioLinks(List.of(link))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
    }

    private UpdateExpertBasicInfoWebRequest givenWebRequest(String profileKey, String backgroundImageFileKey, String nickname, String link, String intro) {
        return UpdateExpertBasicInfoWebRequest.builder()
                .profileImageFileKey(profileKey)
                .backgroundImageFileKey(backgroundImageFileKey)
                .nickname(nickname)
                .link(link)
                .intro(intro)
                .build();
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
