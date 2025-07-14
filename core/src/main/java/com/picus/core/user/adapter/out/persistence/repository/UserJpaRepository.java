package com.picus.core.user.adapter.out.persistence.repository;

import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByProviderAndProviderId(Provider provider, String providerId);

    @Query("select u.role from UserEntity u where u.userNo = :userNo")
    Optional<Role> findRoleById(@Param("userNo") String userNo);
}
