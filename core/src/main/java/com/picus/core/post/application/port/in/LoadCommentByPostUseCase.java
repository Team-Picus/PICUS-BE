package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;

import java.util.List;

public interface LoadCommentByPostUseCase {

    List<LoadCommentByPostResult> load(String postNo);
}
