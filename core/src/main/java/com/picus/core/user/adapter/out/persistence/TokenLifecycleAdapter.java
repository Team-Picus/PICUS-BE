package com.picus.core.user.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.user.application.port.out.TokenLifecycleCommandPort;
import com.picus.core.user.application.port.out.TokenLifecycleQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Objects;

@PersistenceAdapter
@RequiredArgsConstructor
public class TokenLifecycleAdapter implements TokenLifecycleQueryPort, TokenLifecycleCommandPort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean existsByKeyAndValue(String key, String value) {
        String savedValue = redisTemplate.opsForValue().get(key);

        if (savedValue == null)
            return false;

        return Objects.equals(savedValue, value);
    }

    @Override
    public boolean existsByKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void save(String key, Duration duration) {
        redisTemplate.opsForValue().set(key, "", duration);
    }

    @Override
    public void save(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
