package com.picus.core.domain.user.domain.repository;

import com.picus.core.domain.user.domain.entity.User;
import com.picus.core.global.oauth.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.profile.email = :email")
    Optional<User> findByUsername(String username);

    Optional<User> findByProviderIdAndProvider(String providerId, Provider provider);

}
