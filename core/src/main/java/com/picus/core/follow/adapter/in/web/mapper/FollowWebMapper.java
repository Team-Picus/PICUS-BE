package com.picus.core.follow.adapter.in.web.mapper;

import com.picus.core.follow.adapter.in.web.data.response.GetFollowResponse;
import com.picus.core.follow.domain.model.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowWebMapper {

    public GetFollowResponse toResponse(Follow follow) {
        return GetFollowResponse.builder()
                .expertNo(follow.getExpertNo())
                .followedAt(follow.getFollowedAt())
                .build();
    }
}
