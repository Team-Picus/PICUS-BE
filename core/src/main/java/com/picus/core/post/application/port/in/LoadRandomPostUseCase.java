package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.result.LoadRandomPostResult;

import java.util.List;

public interface LoadRandomPostUseCase {

    List<LoadRandomPostResult> load(int size);
}
