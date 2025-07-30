package com.picus.core.moodboard.adapter.out.persistence;

import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardEntity;
import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardId;
import com.picus.core.moodboard.adapter.out.persistence.mapper.MoodboardPersistenceMapper;
import com.picus.core.moodboard.adapter.out.persistence.repository.MoodboardJpaRepository;
import com.picus.core.moodboard.application.port.out.CreateMoodboardPort;
import com.picus.core.moodboard.application.port.out.DeleteMoodboardPort;
import com.picus.core.moodboard.application.port.out.ReadMoodboardPort;
import com.picus.core.moodboard.domain.Moodboard;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PersistenceAdapterReadMoodboard
        implements CreateMoodboardPort, ReadMoodboardPort, DeleteMoodboardPort {

    // DI
    // Repository
    private final MoodboardJpaRepository moodboardJpaRepository;

    // Mapper
    private final MoodboardPersistenceMapper moodboardPersistenceMapper;


    // Methods
    @Override
    public void create(String userNo, String postNo) {
        MoodboardEntity entity = MoodboardEntity.builder()
                .userNo(userNo)
                .postNo(postNo)
                .build();

        moodboardJpaRepository.save(entity);
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

    @Override
    public List<Moodboard> findByUserNo(String userNo) {
        return moodboardJpaRepository.findByUser(userNo).stream()
                .map(moodboardPersistenceMapper::toDomainModel)
                .toList();
    }
}
