package com.picus.core.expert.integration;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertResponse;
import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.shared.IntegrationTestSupport;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SuggestExpertIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProfileImageJpaRepository profileImageJpaRepository;
    @Autowired
    private ExpertJpaRepository expertJpaRepository;

    @AfterEach
    void tearDown() {
        expertJpaRepository.deleteAllInBatch();
        profileImageJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    private record SetupSearchData(
            String expertNo1,
            String expertNo2,
            String expertNo3
    ) {}

    @Test
    @DisplayName("사용자는 특정 키워드가 포함된 전문가들을 n명을 추천받을 수 있다")
    public void searchExpert() throws Exception {
        // given
        String testKeyword = "nick";
        int size = 2;

        String testExpertNo1;
        String testNickname1 = "xxnick1";
//        String testProfileImageUrl1 = "img1.com";

        String testExpertNo2;
        String testNickname2 = "xnick2x";
//        String testProfileImageUrl2 = "img2.com";

        String testExpertNo3;
        String testNickname3 = "nick3xx";
//        String testProfileImageUrl3 = "img3.com";

        SetupSearchData d = setUpData(
                testNickname1, "email1@example.com", "social1",
                testNickname2, "email2@example.com", "social2",
                testNickname3, "email3@example.com", "social3"
        );
        testExpertNo1 = d.expertNo1;
        testExpertNo2 = d.expertNo2;
        testExpertNo3 = d.expertNo3;

        commitTestTransaction(); // 데이터베이스에 저장

        // 요청값 셋팅
        UserEntity userEntity = createUserEntity(); // 인증을 위한 가상의 사용자 생성
        HttpEntity<Void> request = settingWebRequest(userEntity, null);

        // when
        ResponseEntity<BaseResponse<List<SearchExpertResponse>>> response = restTemplate.exchange(
                "/api/v1/experts/search/suggestions?keyword={keyword}&size={size}",
                HttpMethod.GET,
                request,

                new ParameterizedTypeReference<>() {
                },
                testKeyword, size
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<List<SearchExpertResponse>> body = response.getBody();
        assertThat(body).isNotNull();

        List<SearchExpertResponse> result = body.getResult();

        assertThat(result).hasSize(2)
                .extracting(SearchExpertResponse::expertNo,
                        SearchExpertResponse::nickname)
                .containsExactly(
                        Tuple.tuple(testExpertNo3, testNickname3),
                        Tuple.tuple(testExpertNo2, testNickname2)
                );
        result.forEach(r ->
                assertThat(r.profileImageUrl())
                        .isNotNull()
        );
    }

    private SetupSearchData setUpData(
            String nickname1, String email1, String providerId1,
            String nickname2, String email2, String providerId2,
            String nickname3, String email3, String providerId3
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

        UserEntity user3 = givenUserEntity(nickname3, email3, providerId3);
        userJpaRepository.save(user3);
        ProfileImageEntity pi3 = givenProfileImageEntity("file_key3", user3.getUserNo());
        profileImageJpaRepository.save(pi3);

        ExpertEntity exp1 = givenExpertEntity();
        exp1.bindUserEntity(user1);
        expertJpaRepository.save(exp1);

        ExpertEntity exp2 = givenExpertEntity();
        exp2.bindUserEntity(user2);
        expertJpaRepository.save(exp2);

        ExpertEntity exp3 = givenExpertEntity();
        exp3.bindUserEntity(user3);
        expertJpaRepository.save(exp3);

        // expertNo 필드 연결
        user1.assignExpertNo(exp1.getExpertNo());
        user2.assignExpertNo(exp2.getExpertNo());
        user3.assignExpertNo(exp3.getExpertNo());

        return new SetupSearchData(
                exp1.getExpertNo(),
                exp2.getExpertNo(),
                exp3.getExpertNo()
        );
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
                .backgroundImageKey("backgroundImageFileKey")
                .intro("intro")
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(10)
                .lastActivityAt(LocalDateTime.of(2025, 10, 10, 1, 1))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
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
}
