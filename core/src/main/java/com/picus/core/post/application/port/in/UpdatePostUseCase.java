package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.command.UpdatePostCommand;

public interface UpdatePostUseCase {

    void update(UpdatePostCommand updatePostCommand);
}
