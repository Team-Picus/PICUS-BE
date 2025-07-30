package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Post;


public interface PostCreatePort {
    Post save(Post post);
}
