package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.request.WritePostAppReq;

public interface PostInfoCommand {

    void writePost(WritePostAppReq writePostAppReq);
}
