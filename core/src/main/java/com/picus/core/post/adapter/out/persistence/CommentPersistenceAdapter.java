package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.application.port.out.CommentCreatePort;
import com.picus.core.post.domain.Comment;
import com.picus.core.shared.annotation.PersistenceAdapter;

@PersistenceAdapter
public class CommentPersistenceAdapter implements CommentCreatePort {
    @Override
    public Comment save(Comment comment) {
        return null;
    }
}
