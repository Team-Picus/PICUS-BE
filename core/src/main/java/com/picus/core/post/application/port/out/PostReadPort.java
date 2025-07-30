package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostReadPort {
    Optional<Post> findById(String postNo);

    Optional<LocalDateTime> findTopUpdatedAtByExpertNo(String expertNo);

    Optional<Post> findByExpertNoAndIsPinnedTrue(String expertNo);

    List<Post> findTopNByTitleContainingOrderByTitle(String keyword, int size);
}
