package com.picus.core.follow.adapter.out.persistence.mapper;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.follow.adapter.out.persistence.entity.FollowEntity;
import com.picus.core.follow.domain.model.Follow;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class FollowPersistenceMapper {

    public FollowEntity toEntity(UserEntity userEntity, ExpertEntity expertEntity) {
        return FollowEntity.builder()
                .user(userEntity)
                .expert(expertEntity)
                .build();
    }

    public Follow toDomainModel(FollowEntity followEntity) {
        return Follow.builder()
                .userNo(followEntity.getUser().getUserNo())
                .expertNo(followEntity.getExpert().getExpertNo())
                .followedAt(followEntity.getFollowedAt())
                .build();
    }
}
