package com.picus.core.user.adapter.out.persistence;

import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.mapper.ProfileImagePersistenceMapper;
import com.picus.core.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
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
    @DisplayName("")
    public void findUserInfoByExpertNo_success() throws Exception {
        // given

        // when

        // then
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
}