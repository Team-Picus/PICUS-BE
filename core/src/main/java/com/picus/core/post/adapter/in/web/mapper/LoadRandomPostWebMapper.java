package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadRandomPostResponse;
import com.picus.core.post.application.port.in.result.LoadRandomPostResult;
import org.springframework.stereotype.Component;

@Component
public class LoadRandomPostWebMapper {

    public LoadRandomPostResponse toResponse(LoadRandomPostResult result) {
        return LoadRandomPostResponse.builder()
                .postNo(result.postNo())
                .nickname(result.nickname())
                .thumbnailUrl(result.thumbnailUrl())
                .build();
    }
}
