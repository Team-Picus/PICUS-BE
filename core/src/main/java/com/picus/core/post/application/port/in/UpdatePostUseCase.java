package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.request.UpdatePostAppReq;

public interface UpdatePostUseCase {

    void update(UpdatePostAppReq updatePostAppReq);
}
