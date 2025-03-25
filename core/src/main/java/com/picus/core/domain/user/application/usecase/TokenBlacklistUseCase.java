package com.picus.core.domain.user.application.usecase;

import com.picus.core.domain.user.domain.service.BlacklistBackupService;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.jwt.RedisTokenPathPrefix;
import com.picus.core.global.jwt.TokenProvider;
import com.picus.core.global.oauth.entity.Blacklist;
import com.picus.core.global.oauth.entity.RefreshToken;
import com.picus.core.global.oauth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.INVALID_ID_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenBlacklistUseCase {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final BlacklistBackupService blacklistBackupService;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 1. 로그아웃
     * 2. 사용자 탈퇴
     * 3. 사용자 정지
     */
    public void invalidateTokens(String accessToken) {
        Long userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));
        RefreshToken refreshToken = refreshTokenRepository.findByUserNo(userNo);

        invalidateAccessToken(accessToken);
        invalidateRefreshToken(refreshToken.getRefreshToken());
    }

    /**
     *  1. 토큰 재발급
     */
    public void invalidateRefreshToken(String refreshToken) {
        LocalDateTime refreshTokenExpiredAt = tokenProvider.getExpiration(refreshToken);

        redisTemplate.opsForValue().set(
                RedisTokenPathPrefix.REFRESH_TOKEN.getPrefix() + refreshToken, "",
                Duration.between(LocalDateTime.now(), refreshTokenExpiredAt)
        );

        Blacklist blacklist = Blacklist.builder()
                .token(refreshToken)
                .expiredAt(refreshTokenExpiredAt)
                .build();

        blacklistBackupService.save(blacklist);
    }

    public Boolean isTokenInvalidated(String token) {
        return redisTemplate.hasKey(convertToKey(token));
    }

    private void invalidateAccessToken(String accessToken) {
        LocalDateTime accessTokenExpiredAt = tokenProvider.getExpiration(accessToken);

        redisTemplate.opsForValue().set(
                RedisTokenPathPrefix.ACCESS_TOKEN.getPrefix() + accessToken, "",
                Duration.between(LocalDateTime.now(), accessTokenExpiredAt)
        );

        Blacklist blacklist = Blacklist.builder()
                .token(accessToken)
                .expiredAt(accessTokenExpiredAt)
                .build();

        blacklistBackupService.save(blacklist);
    }

    private String convertToKey(String token) {
        if (tokenProvider.isAccessToken(token)) {
            return RedisTokenPathPrefix.ACCESS_TOKEN.getPrefix() + token;
        } else {
            return RedisTokenPathPrefix.REFRESH_TOKEN.getPrefix() + token;
        }
    }
}
