package com.picus.core.user.adapter.out.persistence.repository;

import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByProviderAndProviderId(Provider provider, String providerId);
}
