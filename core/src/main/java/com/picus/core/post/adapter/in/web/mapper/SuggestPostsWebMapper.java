package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostsResponse;
import com.picus.core.post.application.port.in.response.SuggestPostsResult;
import org.springframework.stereotype.Component;

@Component
public class SuggestPostsWebMapper {

    public SuggestPostsResponse toResponse(SuggestPostsResult result) {
        return SuggestPostsResponse.builder()
                .postId(result.postNo())
                .title(result.title())
                .build();
    }

}
