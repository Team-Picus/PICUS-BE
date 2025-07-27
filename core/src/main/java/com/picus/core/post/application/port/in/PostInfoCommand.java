package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.in.request.WritePostAppReq;

public interface PostInfoCommand {

    void write(WritePostAppReq writePostAppReq);

    void update(UpdatePostAppReq updatePostAppReq);

    void delete(String postNo, String currentUserNo);
}
