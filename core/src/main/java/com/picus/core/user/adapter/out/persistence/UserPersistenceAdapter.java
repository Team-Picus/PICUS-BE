package com.picus.core.user.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserCommandPort, UserQueryPort {

    private final UserJpaRepository userJpaRepository;
    private final ProfileImageJpaRepository profileImageJpaRepository;

    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public User upsert(User user) {
        UserEntity entity = userJpaRepository
                .findByProviderAndProviderId(user.getAuth().getProvider(), user.getAuth().getProviderId())
                .map(existing -> {
                    existing.updateSocialProfile(user.getEmail(), user.getName(), user.getTel());
                    return existing;
                })
                .orElseGet(() ->
                        userPersistenceMapper.toEntity(user)
                );

        UserEntity savedEntity = userJpaRepository.save(entity);
        return userPersistenceMapper.toDomainModel(savedEntity);
    }

    @Override
    public void save(User user) {
        UserEntity entity = userPersistenceMapper.toEntity(user);
        userJpaRepository.save(entity);
    }

    @Override
    public void assignExpertNo(String userNo, String expertNo) {
        UserEntity userEntity = userJpaRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        userEntity.assignExpertNo(expertNo);
    }

    @Override
    public void updateNicknameAndImageByExpertNo(UserWithProfileImageDto userWithProfileImageDto) {
        String expertNo = userWithProfileImageDto.expertNo();

        // 닉네임 수정
        UserEntity userEntity = userJpaRepository.findByExpertNo(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        userEntity.updateNickname(userWithProfileImageDto.nickname());

        // 프로필 이미지 수정
        ProfileImageEntity profileImageEntity = profileImageJpaRepository.findByUserNo(userEntity.getUserNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        profileImageEntity.updateFileKey(userWithProfileImageDto.profileImageFileKey());
    }

    @Override
    public User findById(String userNo) {
        return userJpaRepository.findById(userNo)
                .map(userPersistenceMapper::toDomainModel)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    @Override
    public Role findRoleById(String userNo) {
        return userJpaRepository.findRoleById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    @Override
    public Boolean existsById(String userNo) {
        return userJpaRepository.existsById(userNo);
    }

    @Override
    public Optional<UserWithProfileImageDto> findUserInfoByExpertNo(String expertNo) {
        return userJpaRepository.findUserInfoByExpertNo(expertNo);
    }

    @Override
    public List<UserWithProfileImageDto> findUserInfoByNicknameContaining(String keyword) {
        return userJpaRepository.findByNicknameContaining(keyword);
    }

    @Override
    public List<UserWithProfileImageDto> findUserInfoByNicknameContainingOrderByNicknameLimited(String keyword, int size) {
        return userJpaRepository.findByNicknameContainingLimited(keyword, PageRequest.of(0, size));
    }
}
