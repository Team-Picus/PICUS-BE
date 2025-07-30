package com.picus.core.moodboard.adapter.out.persistence.mapper;

import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardEntity;
import com.picus.core.moodboard.domain.Moodboard;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MoodboardPersistenceMapper {

    public Moodboard toDomainModel(MoodboardEntity entity) {
        return Moodboard.builder()
                .userNo(entity.getUserNo())
                .postNo(entity.getPostNo())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
