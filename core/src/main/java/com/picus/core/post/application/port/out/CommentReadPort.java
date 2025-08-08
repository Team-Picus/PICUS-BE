package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentReadPort {

    Optional<Comment> findById(String commentNo);

    List<Comment> findByPostNo(String postNo);
}
