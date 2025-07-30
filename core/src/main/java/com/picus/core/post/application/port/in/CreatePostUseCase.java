package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.command.CreatePostCommand;

public interface CreatePostUseCase {
    void create(CreatePostCommand createPostCommand);
}
