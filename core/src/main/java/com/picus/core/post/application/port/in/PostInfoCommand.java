package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.request.WritePostAppReq;

public interface PostInfoCommand {

    void write(WritePostAppReq writePostAppReq);
}
