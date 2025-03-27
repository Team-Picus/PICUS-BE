package com.picus.core.global.ratelimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RequestRateLimiter {
    private final static String REDIS_KEY_PREFIX = "REQUEST_RATE_LIMITER:";
    private final RedisTemplate<String, String> redisTemplate;

    public boolean isRequestAllowed(String userNo, String url) {
        String redisKey = REDIS_KEY_PREFIX + url + ":" + userNo;
        if (!Objects.isNull(redisTemplate.opsForValue().get(redisKey)))
            return false;

        saveRequest(redisKey);
        return true;
    }

    private void saveRequest(String key) {
        redisTemplate.opsForValue().set(key,
                "",
                Duration.between(LocalDateTime.now(),LocalDateTime.now().plus(500, ChronoUnit.MILLIS))
        );
    }
}
