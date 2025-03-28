package com.picus.core.global.oauth.repository;

import com.picus.core.global.oauth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserNo(Long userId);
//    RefreshToken findByUserNoAndRefreshToken(String userId, String refreshToken);
}
