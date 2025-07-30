package com.picus.core.follow.adapter.in.web.mapper;

import com.picus.core.follow.adapter.in.web.data.response.LoadFollowResponse;
import com.picus.core.follow.domain.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowWebMapper {

    public LoadFollowResponse toResponse(Follow follow) {
        return LoadFollowResponse.builder()
                .expertNo(follow.getExpertNo())
                .followedAt(follow.getFollowedAt())
                .build();
    }
}
