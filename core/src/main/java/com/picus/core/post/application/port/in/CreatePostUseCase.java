package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.request.CreatePostAppReq;

public interface CreatePostUseCase {
    void create(CreatePostAppReq createPostAppReq);
}
