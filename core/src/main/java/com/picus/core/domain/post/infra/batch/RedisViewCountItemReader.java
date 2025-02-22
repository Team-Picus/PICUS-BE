package com.picus.core.domain.post.infra.batch;

import com.picus.core.domain.post.domain.entity.view.ViewCount;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Iterator;
import java.util.Set;

public class RedisViewCountItemReader implements ItemReader<ViewCount> {

    private final RedisTemplate<String, Integer> redisTemplate;
    private Iterator<ViewCount> iterator;

    public RedisViewCountItemReader(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ViewCount read() throws Exception {
        if (iterator == null) {
            Set<String> keys = redisTemplate.keys(ViewCount.VIEW_COUNT_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                return null;
            }

            iterator = keys.stream()
                    .map(key -> new ViewCount(key, redisTemplate.opsForValue().get(key)))
                    .toList()
                    .iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}
