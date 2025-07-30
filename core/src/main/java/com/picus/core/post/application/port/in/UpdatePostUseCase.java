package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.request.UpdatePostCommand;

public interface UpdatePostUseCase {

    void update(UpdatePostCommand updatePostCommand);
}
