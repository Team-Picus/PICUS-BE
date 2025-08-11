package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.result.LoadPostDetailResult;

public interface LoadPostDetailUseCase {
    LoadPostDetailResult load(String postNo);
}
