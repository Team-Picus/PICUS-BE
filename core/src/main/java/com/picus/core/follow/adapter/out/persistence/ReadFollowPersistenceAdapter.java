package com.picus.core.follow.adapter.out.persistence;

import com.picus.core.follow.adapter.out.persistence.entity.FollowEntity;
import com.picus.core.follow.adapter.out.persistence.entity.FollowId;
import com.picus.core.follow.adapter.out.persistence.mapper.FollowPersistenceMapper;
import com.picus.core.follow.adapter.out.persistence.repository.FollowJpaRepository;
import com.picus.core.follow.application.port.out.CreateFollowPort;
import com.picus.core.follow.application.port.out.DeleteFollowPort;
import com.picus.core.follow.application.port.out.ReadFollowPort;
import com.picus.core.follow.domain.Follow;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReadFollowPersistenceAdapter
        implements ReadFollowPort, CreateFollowPort, DeleteFollowPort {

    private final FollowJpaRepository followJpaRepository;
    private final FollowPersistenceMapper followPersistenceMapper;

    @Override
    public Boolean existsById(String userNo, String expertNo) {
        return followJpaRepository.existsById(new FollowId(userNo, expertNo));
    }

    @Override
    public List<Follow> findByUserNo(String userNo) {
        return followJpaRepository.findByUserNo(userNo).stream()
                .map(followPersistenceMapper::toDomainModel)
                .toList();
    }

    @Override
    public void create(String userNo, String expertNo) {
        FollowEntity entity = FollowEntity.builder()
                .userNo(userNo)
                .expertNo(expertNo)
                .build();

        followJpaRepository.save(entity);
    }

    @Override
    public void delete(String userNo, String expertNo) {
        followJpaRepository.deleteById(new FollowId(userNo, expertNo));
    }
}
