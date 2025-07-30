package com.picus.core.user.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.adapter.out.persistence.entity.TermEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserTermEntity;
import com.picus.core.user.adapter.out.persistence.mapper.TermPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.mapper.UserTermPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.TermJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.adapter.out.persistence.repository.UserTermJpaRepository;
import com.picus.core.user.application.port.out.UserTermCreatePort;
import com.picus.core.user.application.port.out.TermReadPort;
import com.picus.core.user.domain.model.Term;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserTermPersistenceAdapter implements UserTermCreatePort {

    private final TermJpaRepository termJpaRepository;
    private final UserTermJpaRepository userTermJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserTermPersistenceMapper userTermPersistenceMapper;

    @Override
    public void create(String userNo, String termNo, Boolean isAgreed) {
        UserEntity userEntity = userJpaRepository.getReferenceById(userNo);
        TermEntity termEntity = termJpaRepository.getReferenceById(termNo);

        UserTermEntity entity = userTermPersistenceMapper.toEntity(userEntity, termEntity, isAgreed);
        userTermJpaRepository.save(entity);
    }
}
