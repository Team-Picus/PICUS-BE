package com.picus.core.global.common.category.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TodayAvailableCategoryService {

    private final String PREFIX = "TODAY_AVAILABLE:";
    private final RedisTemplate<String, String> redisTemplate;

    public void activate(List<Long> postIds) {
        redisTemplate.opsForValue().multiSet(convertToMapWithPrefix(postIds));
    }

    public void activate(List<Long> postIds, Duration duration) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            postIds.stream()
                    .map(postId -> PREFIX + postId)
                    .forEach(keyStr -> {
                        byte[] key = redisTemplate.getStringSerializer().serialize(keyStr);
                        byte[] value = redisTemplate.getStringSerializer().serialize("");
                        connection.setEx(key, duration.getSeconds(), value);
                    });
            return null;
        });
    }

    public void deactivate(Long postId) {
        redisTemplate.delete(PREFIX + postId);
    }

    private Map<String, String> convertToMapWithPrefix(List<Long> list) {
        return IntStream.range(0, list.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> PREFIX + list.get(i),
                        i -> ""
                ));
    }
}
