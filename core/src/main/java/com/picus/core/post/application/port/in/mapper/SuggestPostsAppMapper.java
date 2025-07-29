package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.response.SuggestPostsAppResp;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class SuggestPostsAppMapper {

    public SuggestPostsAppResp toAppResp(Post post) {
        return SuggestPostsAppResp.builder()
                .postNo(post.getPostNo())
                .title(post.getTitle())
                .build();
    }
}
