package com.picus.core.domain.post.repository.view.primary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class ViewCountCustomRepositoryImpl implements ViewCountCustomRepository {

    private final RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Optional<Integer> findViewCount(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
