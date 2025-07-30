package com.picus.core.follow.adapter.out.persistence.mapper;

import com.picus.core.follow.adapter.out.persistence.entity.FollowEntity;
import com.picus.core.follow.domain.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowPersistenceMapper {

    public Follow toDomainModel(FollowEntity followEntity) {
        return Follow.builder()
                .userNo(followEntity.getUserNo())
                .expertNo(followEntity.getExpertNo())
                .followedAt(followEntity.getFollowedAt())
                .build();
    }
}
