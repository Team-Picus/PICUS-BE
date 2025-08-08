package com.picus.core.post.application.port.in;

public interface DeleteCommentUseCase {

    void delete(String commentNo, String currentUserNo);
}
