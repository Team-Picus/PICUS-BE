package com.picus.core.moodboard.adapter.out.persistence.mapper;

import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardEntity;
import com.picus.core.moodboard.domain.model.Moodboard;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MoodboardPersistenceMapper { // DB Entity <-> Domain Model

    public MoodboardEntity toEntity(UserEntity userEntity, PostEntity postEntity) {
        return MoodboardEntity.builder()
                .user(userEntity)
                .post(postEntity)
                .build();
    }

    public Moodboard toDomainModel(MoodboardEntity entity) {
        return Moodboard.builder()
                .moodboardNo
                .build();
    }
}
