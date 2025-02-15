package com.picus.core.domain.post.entity.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class ViewHistory {

    public static final String VIEW_HISTORY_PREFIX = "VIEW_HISTORY:";

    @Id
    private String key;

    private Object empty;

    public static String generateKey(Long userId, Long postId) {
        return VIEW_HISTORY_PREFIX + userId + ":" + postId;
    }
}