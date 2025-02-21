package com.picus.core.domain.post.application.usecase;

import com.picus.core.domain.post.domain.entity.view.ViewCount;
import com.picus.core.global.common.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewTrackerUseCase {

    private final RedisTemplate<String, Integer> redisTemplate;

    public Integer findViewCount(Long postId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(ViewCount.generateKey(postId)))
                .orElseThrow(PostNotFoundException::new);
    }

    private void incrementViewCount(Long postId) {
        redisTemplate.opsForValue().increment(ViewCount.generateKey(postId));
    }
}
