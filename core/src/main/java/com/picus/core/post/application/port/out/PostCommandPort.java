package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.model.Post;

public interface PostCommandPort {
    void save(Post post);
}
