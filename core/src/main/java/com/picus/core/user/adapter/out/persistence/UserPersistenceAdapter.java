package com.picus.core.user.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserCommandPort, UserQueryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public User upsert(String providerId, Provider provider, String email) {
        UserEntity entity = userJpaRepository
                .findByProviderAndProviderId(provider, providerId)
                .map(existing -> {
                    existing.updateSocialProfile(email);
                    return existing;
                })
                .orElseGet(() ->
                        userPersistenceMapper.mapToUserEntity(providerId, provider, email)
                );

        UserEntity savedEntity = userJpaRepository.save(entity);
        return userPersistenceMapper.mapToUser(savedEntity);
    }

    @Override
    public User findById(String userNo) {
        return userJpaRepository.findById(userNo)
                .map(userPersistenceMapper::mapToUser)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
