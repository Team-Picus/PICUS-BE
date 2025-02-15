package com.picus.core.domain.post.repository.view.primary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ViewHistoryCustomRepositoryImpl implements ViewHistoryCustomRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String key) {
        redisTemplate.opsForValue().set(key, "", 5, TimeUnit.MINUTES);
    }
}
