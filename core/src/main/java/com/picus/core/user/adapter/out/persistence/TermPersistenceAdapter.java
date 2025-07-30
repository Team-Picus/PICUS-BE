package com.picus.core.user.adapter.out.persistence;

import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.adapter.out.persistence.mapper.TermPersistenceMapper;
import com.picus.core.user.adapter.out.persistence.repository.TermJpaRepository;
import com.picus.core.user.application.port.out.TermReadPort;
import com.picus.core.user.domain.model.Term;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class TermPersistenceAdapter implements TermReadPort {

    private final TermJpaRepository termJpaRepository;
    private final TermPersistenceMapper termPersistenceMapper;

    @Override
    public List<Term> findAll() {
        return termJpaRepository.findAll().stream()
                .map(termPersistenceMapper::toDomainModel)
                .toList();
    }

    @Override
    public Term findById(String termNo) {
        return termJpaRepository.findById(termNo)
                .map(termPersistenceMapper::toDomainModel)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
