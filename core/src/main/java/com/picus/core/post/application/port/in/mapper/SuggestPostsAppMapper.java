package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.response.SuggestPostsResult;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class SuggestPostsAppMapper {

    public SuggestPostsResult toAppResp(Post post) {
        return SuggestPostsResult.builder()
                .postNo(post.getPostNo())
                .title(post.getTitle())
                .build();
    }
}
