package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Comment;

public interface CommentCreatePort {

    Comment save(Comment comment);
}
