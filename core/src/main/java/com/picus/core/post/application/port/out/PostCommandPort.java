package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.model.Post;

import java.util.List;

public interface PostCommandPort {
    Post save(Post post);

    void update(Post post, List<String> deletedPostImageNos);

    void delete(String postNo);
}
