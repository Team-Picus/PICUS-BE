package com.picus.core.domain.post.domain.entity.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "VIEW_HISTORY", timeToLive = 300)
public class ViewHistory {

    @Id
    private String key;

    private Object empty;

    public ViewHistory(Long userId, Long postId) {
        this.key = format(userId, postId);
        this.empty = "";
    }

    public static String format(Long userId, Long postId) {
        return userId + ":" + postId;
    }
}
