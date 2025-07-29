package com.picus.core.user.adapter.out.persistence;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.mapper.ProfileImagePersistenceMapper;
import com.picus.core.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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

        clearPersistenceContext();

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

        clearPersistenceContext();

        // when
        Optional<UserWithProfileImageDto> optionalResult = userPersistenceAdapter.findUserInfoByExpertNo(expertNo);

        // then
        assertThat(optionalResult).isPresent();
        UserWithProfileImageDto result = optionalResult.get();

        assertThat(result.nickname()).isEqualTo(testNickname);
        assertThat(result.profileImageFileKey()).isEqualTo(testProfileImageFileKey);
        assertThat(result.expertNo()).isEqualTo(expertNo);
    }

    @Test
    @DisplayName("특정 닉네임 키워드가 포함된 사용자의 정보를 오름차순 조회한다.")
    public void findUserInfoByNicknameContaining() throws Exception {
        // given
        String keyword = "nickname";

        String testNickname1 = "xxnickname";
        String testFileKey1 = "file_key1";
        String testExpertNo1 = "exp1";

        String testNickname2 = "xnicknamex";
        String testFileKey2 = "file_key2";
        String testExpertNo2 = "exp2";

        String testNickname3 = "nicknamexx";
        String testFileKey3 = "file_key3";
        String testExpertNo3 = "exp3";

        String testNickname4 = "xnicknamx";
        String testFileKey4 = "file_key4";
        String testExpertNo4 = "exp4";

        // 데이터 셋팅
        settingDataAtFindUserInfoByNicknameContaining(
                testNickname1, testExpertNo1,
                testNickname2, testExpertNo2,
                testNickname3, testExpertNo3,
                testNickname4, testExpertNo4,
                testFileKey1, testFileKey2, testFileKey3, testFileKey4);


        // when
        List<UserWithProfileImageDto> results = userPersistenceAdapter.findUserInfoByNicknameContaining(keyword);

        // then
        assertThat(results).hasSize(3);
        assertThat(results).extracting(
                UserWithProfileImageDto::nickname,
                UserWithProfileImageDto::profileImageFileKey,
                UserWithProfileImageDto::expertNo)
                .containsExactly(
                        tuple(testNickname3, testFileKey3, testExpertNo3),
                        tuple(testNickname2, testFileKey2, testExpertNo2),
                        tuple(testNickname1, testFileKey1, testExpertNo1)
                );
    }

    @Test
    @DisplayName("특정 닉네임 키워드가 포함된 전문가를 n개를 이름순으로 오름차순 조회한다.")
    public void findUserInfoByNicknameContainingOrderByNicknameLimited() throws Exception {
        // given
        String keyword = "nickname";
        int size = 2;

        String testNickname1 = "xxnickname";
        String testFileKey1 = "file_key1";
        String testExpertNo1 = "exp1";

        String testNickname2 = "xnicknamex";
        String testFileKey2 = "file_key2";
        String testExpertNo2 = "exp2";

        String testNickname3 = "nicknamexx";
        String testFileKey3 = "file_key3";
        String testExpertNo3 = "exp3";

        String testNickname4 = "xnicknamx";
        String testFileKey4 = "file_key4";
        String testExpertNo4 = "exp4";

        // 데이터 셋팅
        settingDataAtFindUserInfoByNicknameContaining(
                testNickname1, testExpertNo1,
                testNickname2, testExpertNo2,
                testNickname3, testExpertNo3,
                testNickname4, testExpertNo4,
                testFileKey1, testFileKey2, testFileKey3, testFileKey4);

        // when
        List<UserWithProfileImageDto> results = userPersistenceAdapter.findUserInfoByNicknameContainingOrderByNicknameLimited(keyword, size);

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting(
                        UserWithProfileImageDto::nickname,
                        UserWithProfileImageDto::profileImageFileKey,
                        UserWithProfileImageDto::expertNo)
                .containsExactlyInAnyOrder(
                        tuple(testNickname3, testFileKey3, testExpertNo3),
                        tuple(testNickname2, testFileKey2, testExpertNo2)
                );
    }

    @Test
    @DisplayName("User의 nickname과 ProfileImage의 file_key를 업데이트 한다.")
    public void updateNicknameAndImageByExpertNo() throws Exception {
        // given
        UserEntity userEntity = givenUserEntity("old_nick", "expert_no");
        userJpaRepository.save(userEntity);
        ProfileImageEntity profileImageEntity = givenProfileImageEntity("old_file_key", userEntity.getUserNo());
        profileImageJpaRepository.save(profileImageEntity);
        clearPersistenceContext();

        UserWithProfileImageDto dto = UserWithProfileImageDto.builder()
                .nickname("new_nick")
                .profileImageFileKey("new_file_key")
                .expertNo("expert_no")
                .build();

        // when
        userPersistenceAdapter.updateNicknameAndImageByExpertNo(dto);
        clearPersistenceContext();

        // then
        UserEntity updatedUser = userJpaRepository.findById(userEntity.getUserNo()).orElseThrow();
        assertThat(updatedUser.getNickname()).isEqualTo("new_nick");

        ProfileImageEntity updatedProfile = profileImageJpaRepository.findById(profileImageEntity.getProfileImageNo()).orElseThrow();
        assertThat(updatedProfile.getFile_key()).isEqualTo("new_file_key");
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

    private UserEntity givenUserEntity(String nickname, String name, String email, String providerId, String expertNo) {
        return UserEntity.builder()
                .name(name)
                .nickname(nickname)
                .tel("01012345678")
                .role(Role.CLIENT)
                .email(email)
                .providerId(providerId)
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .expertNo(expertNo)
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

    private UserEntity givenUserEntity(String nickname, String expertNo) {
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
                .expertNo(expertNo)
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

    private void settingDataAtFindUserInfoByNicknameContaining(String testNickname1, String testExpertNo1, String testNickname2, String testExpertNo2, String testNickname3, String testExpertNo3, String testNickname4, String testExpertNo4, String testFileKey1, String testFileKey2, String testFileKey3, String testFileKey4) {
        UserEntity userEntity1 = givenUserEntity(testNickname1, "name1", "email1@example.com", "social1", testExpertNo1);
        UserEntity userEntity2 = givenUserEntity(testNickname2, "name2", "email2@example.com", "social2", testExpertNo2);
        UserEntity userEntity3 = givenUserEntity(testNickname3, "name3", "email3@example.com", "social3", testExpertNo3);
        UserEntity userEntity4 = givenUserEntity(testNickname4, "name4", "email4@example.com", "social4", testExpertNo4);
        userJpaRepository.saveAll(List.of(userEntity1, userEntity2, userEntity3, userEntity4));

        ProfileImageEntity profileImageEntity1 = givenProfileImageEntity(testFileKey1, userEntity1.getUserNo());
        ProfileImageEntity profileImageEntity2 = givenProfileImageEntity(testFileKey2, userEntity2.getUserNo());
        ProfileImageEntity profileImageEntity3 = givenProfileImageEntity(testFileKey3, userEntity3.getUserNo());
        ProfileImageEntity profileImageEntity4 = givenProfileImageEntity(testFileKey4, userEntity4.getUserNo());
        profileImageJpaRepository.saveAll(List.of(profileImageEntity1, profileImageEntity2, profileImageEntity3, profileImageEntity4));
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}