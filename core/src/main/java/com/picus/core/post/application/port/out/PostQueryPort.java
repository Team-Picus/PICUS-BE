package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Post;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostQueryPort {
    Optional<Post> findById(String postNo);

    Optional<LocalDateTime> findTopUpdatedAtByExpertNo(String authorNo);
}
