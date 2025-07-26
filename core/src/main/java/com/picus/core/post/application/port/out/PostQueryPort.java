package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.model.Post;

import java.util.Optional;

public interface PostQueryPort {
    Optional<Post> findById(String postNo);
}
