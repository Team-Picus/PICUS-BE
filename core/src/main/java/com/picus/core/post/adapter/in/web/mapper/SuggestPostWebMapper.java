package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostResponse;
import com.picus.core.post.application.port.in.result.SuggestPostResult;
import org.springframework.stereotype.Component;

@Component
public class SuggestPostWebMapper {

    public SuggestPostResponse toResponse(SuggestPostResult result) {
        return SuggestPostResponse.builder()
                .postNo(result.postNo())
                .title(result.title())
                .build();
    }

}
