package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.request.CreatePostCommand;

public interface CreatePostUseCase {
    void create(CreatePostCommand createPostCommand);
}
