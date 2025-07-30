package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Post;

import java.util.List;

public interface PostUpdatePort {

    void update(Post post);

    void updateWithPostImage(Post post, List<String> deletedPostImageNos);
}
