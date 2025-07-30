package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.result.SuggestPostResult;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class SuggestPostCommandMapper {

    public SuggestPostResult toResult(Post post) {
        return SuggestPostResult.builder()
                .postNo(post.getPostNo())
                .title(post.getTitle())
                .build();
    }
}
