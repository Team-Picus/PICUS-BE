package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.assertj.core.groups.Tuple;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class SearchExpertIntegrationTest {
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

    private record SetupSearchData(
            String expertNo1,
            String expertNo2
    ) {}

    @Test
    @DisplayName("사용자는 특정 키워드가 포함된 전문가들을 검색할 수 있다.")
    public void searchExpert() throws Exception {
        // given
        String testKeyword = "nick";

        String testExpertNo1;
        String testNickname1 = "xxnick1";
//        String testProfileImageUrl1 = "img1.com";

        String testExpertNo2;
        String testNickname2 = "xnick2x";
//        String testProfileImageUrl2 = "img2.com";

        SetupSearchData d = setUpData(
                testNickname1, "email1@example.com", "social1",
                testNickname2, "email2@example.com", "social2"
        );
        testExpertNo1 = d.expertNo1;
        testExpertNo2 = d.expertNo2;

        commitTestTransaction();

        // 요청값 셋팅
        HttpEntity<Void> request = setUpRequest();

        // when
        ResponseEntity<BaseResponse<List<SearchExpertWebResponse>>> response = restTemplate.exchange(
                "/api/v1/experts/search/results?keyword={keyword}",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                testKeyword
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<List<SearchExpertWebResponse>> body = response.getBody();
        assertThat(body).isNotNull();

        List<SearchExpertWebResponse> result = body.getResult();

        assertThat(result).hasSize(2)
                .extracting("expertNo", "nickname")
                .containsExactly(
                        Tuple.tuple(testExpertNo2, testNickname2),
                        Tuple.tuple(testExpertNo1, testNickname1)
                );
        result.forEach(r ->
                assertThat(r.profileImageUrl())
                        .isNotNull()
        );
    }

    private SetupSearchData setUpData(
            String nickname1, String email1, String providerId1,
            String nickname2, String email2, String providerId2
    ) {
        // User + ProfileImage + Expert 엔티티 생성 및 저장
        UserEntity user1 = givenUserEntity(nickname1, email1, providerId1);
        userJpaRepository.save(user1);
        ProfileImageEntity pi1 = givenProfileImageEntity("file_key1", user1.getUserNo());
        profileImageJpaRepository.save(pi1);

        UserEntity user2 = givenUserEntity(nickname2, email2, providerId2);
        userJpaRepository.save(user2);
        ProfileImageEntity pi2 = givenProfileImageEntity("file_key2", user2.getUserNo());
        profileImageJpaRepository.save(pi2);

        ExpertEntity exp1 = givenExpertEntity();
        exp1.bindUserEntity(user1);
        expertJpaRepository.save(exp1);

        ExpertEntity exp2 = givenExpertEntity();
        exp2.bindUserEntity(user2);
        expertJpaRepository.save(exp2);

        // expertNo 필드 연결
        user1.assignExpertNo(exp1.getExpertNo());
        user2.assignExpertNo(exp2.getExpertNo());

        return new SetupSearchData(
                exp1.getExpertNo(),
                exp2.getExpertNo()
        );
    }

    private HttpEntity<Void> setUpRequest() {
        String accessToken = tokenProvider.createAccessToken("test_id", "ROLE_USER");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        return request;
    }

    private UserEntity givenUserEntity(String nickname, String email, String providerId) {
        return UserEntity.builder()
                .name("이름")
                .nickname(nickname)
                .tel("01012345678")
                .role(Role.CLIENT)
                .email(email)
                .providerId(providerId)
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

    private ExpertEntity givenExpertEntity() {
        return ExpertEntity.builder()
                .backgroundImageKey("backgroundImageKey")
                .intro("intro")
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(10)
                .lastActivityAt(LocalDateTime.of(2025, 10, 10, 1, 1))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }

}
