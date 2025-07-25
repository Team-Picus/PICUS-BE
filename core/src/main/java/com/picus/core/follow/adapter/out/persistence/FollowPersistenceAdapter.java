package com.picus.core.follow.adapter.out.persistence;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.follow.adapter.out.persistence.entity.FollowEntity;
import com.picus.core.follow.adapter.out.persistence.entity.FollowId;
import com.picus.core.follow.adapter.out.persistence.mapper.FollowPersistenceMapper;
import com.picus.core.follow.adapter.out.persistence.repository.FollowJpaRepository;
import com.picus.core.follow.application.port.out.FollowCommandPort;
import com.picus.core.follow.application.port.out.FollowQueryPort;
import com.picus.core.follow.domain.model.Follow;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements FollowQueryPort, FollowCommandPort {

    private final FollowJpaRepository followJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ExpertJpaRepository expertJpaRepository;
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
    public void save(String userNo, String expertNo) {
        UserEntity userEntity = userJpaRepository.getReferenceById(userNo);
        ExpertEntity expertEntity = expertJpaRepository.getReferenceById(expertNo);

        FollowEntity entity = followPersistenceMapper.toEntity(userEntity, expertEntity);
        followJpaRepository.save(entity);
    }

    @Override
    public void delete(String userNo, String expertNo) {
        followJpaRepository.deleteById(new FollowId(userNo, expertNo));
    }
}
