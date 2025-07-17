package com.picus.core.user.adapter.out.persistence;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.mapper.ProfileImagePersistenceMapper;
import com.picus.core.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.application.port.out.response.UserWithProfileImageDto;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@Import({
        UserPersistenceAdapter.class,
        UserPersistenceMapper.class,
        ProfileImagePersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserPersistenceAdapterTest {

    @Autowired
    UserPersistenceAdapter userPersistenceAdapter;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ExpertJpaRepository expertJpaRepository;
    @Autowired
    ProfileImageJpaRepository profileImageJpaRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("해당 User에 ExpertNo를 할당한다.")
    public void assignExpertNo_success() throws Exception {
        // given
        String expertNo = "expert_no";
        UserEntity userEntity = givenUserEntity();
        UserEntity saved = userJpaRepository.save(userEntity);
        String userNo = saved.getUserNo();
        em.flush();
        em.clear();

        // when
        userPersistenceAdapter.assignExpertNo(userNo, expertNo);


        // then
        UserEntity updated = userJpaRepository.findById(userNo).orElseThrow();
        assertThat(updated.getExpertNo()).isEqualTo(expertNo);
    }

    @Test
    @DisplayName("ExpertNo을 가지고 사용자의 닉네임과 프로필이미지 파일키를 조회한다.")
    public void findUserInfoByExpertNo_success() throws Exception {
        // given
        String testNickname = "nick"; // 테스트 데이터
        String testProfileImageFileKey = "profile_key";

        UserEntity userEntity = givenUserEntity(testNickname); // UserEntity 저장
        userJpaRepository.save(userEntity);

        ProfileImageEntity profileImageEntity = givenProfileImageEntity(testProfileImageFileKey, userEntity.getUserNo()); // ProfileImageEntity 저장
        profileImageJpaRepository.save(profileImageEntity);

        ExpertEntity expertEntity = givenExpertEntity(); // ExpertEntity 저장
        expertEntity.bindUserEntity(userEntity);
        expertJpaRepository.save(expertEntity);

        String expertNo = expertEntity.getExpertNo();
        userEntity.assignExpertNo(expertNo); // UserEntity에 ExpertNo 할당

        em.flush(); // 영속성 컨텍스트 정리
        em.clear();

        // when
        Optional<UserWithProfileImageDto> optionalResult = userPersistenceAdapter.findUserInfoByExpertNo(expertNo);

        // then
        assertThat(optionalResult).isPresent();
        UserWithProfileImageDto result = optionalResult.get();
        
        assertThat(result.nickname()).isEqualTo(testNickname);
        assertThat(result.profileImageFileKey()).isEqualTo(testProfileImageFileKey);
        assertThat(result.expertNo()).isEqualTo(expertNo);
    }

    private UserEntity givenUserEntity() {
        return UserEntity.builder()
                .name("이름")
                .nickname("닉네임")
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

    private ExpertEntity givenExpertEntity() {
        return ExpertEntity.builder()
                .backgroundImageKey("img-key")
                .intro("전문가 소개")
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(8)
                .lastActivityAt(LocalDateTime.of(2024, 5, 20, 10, 30))
                .portfolioLinks(List.of("http://myportfolio.com"))
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }
}