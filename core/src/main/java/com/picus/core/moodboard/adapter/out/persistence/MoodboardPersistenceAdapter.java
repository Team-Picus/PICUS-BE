package com.picus.core.moodboard.adapter.out.persistence;

import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardEntity;
import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardId;
import com.picus.core.moodboard.adapter.out.persistence.mapper.MoodboardPersistenceMapper;
import com.picus.core.moodboard.adapter.out.persistence.repository.MoodboardJpaRepository;
import com.picus.core.moodboard.application.port.out.MoodboardCommandPort;
import com.picus.core.moodboard.application.port.out.MoodboardQueryPort;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MoodboardPersistenceAdapter implements MoodboardCommandPort, MoodboardQueryPort {

    private final UserJpaRepository userJpaRepository;
    private final MoodboardPersistenceMapper moodboardPersistenceMapper;
    private final MoodboardJpaRepository moodboardJpaRepository;

    @Override
    public void save(String userNo, String postNo) {
        UserEntity userEntity = userJpaRepository.getReferenceById(userNo);
        // PostEntity postEntity = postJpaRepository.getReferenceById(postNo);

//        MoodboardEntity entity = moodboardPersistenceMapper.toEntity(userEntity, postEntity);
//        moodboardJpaRepository.save(entity);
    }

    @Override
    public void delete(String userNo, String postNo) {
        MoodboardId moodboardId = new MoodboardId(userNo, postNo);
        moodboardJpaRepository.deleteById(moodboardId);
    }

    @Override
    public boolean existsById(String userNo, String postNo) {
        MoodboardId moodboardId = new MoodboardId(userNo, postNo);
        return moodboardJpaRepository.existsById(moodboardId);
    }
}
