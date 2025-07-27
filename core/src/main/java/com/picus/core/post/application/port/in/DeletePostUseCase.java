package com.picus.core.post.application.port.in;

public interface DeletePostUseCase {

    void delete(String postNo, String currentUserNo);
}
