package com.picus.core.domain.post.entity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class ViewCount {

    public static final String VIEW_COUNT_PREFIX = "VIEW_COUNT:";

    @Id
    private String key;

    private Integer count;

    public static String generateKey(Long postId) {
        return VIEW_COUNT_PREFIX + postId;
    }

    public static Long extractPostId(String key) {
        return Long.parseLong(key.substring(VIEW_COUNT_PREFIX.length()));
    }
}