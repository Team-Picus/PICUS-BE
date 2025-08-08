package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.command.CreateCommentCommand;

public interface CreateCommentUseCase {

    void create(CreateCommentCommand command);
}
