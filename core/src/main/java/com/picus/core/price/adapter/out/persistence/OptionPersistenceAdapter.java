package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.mapper.OptionPersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.application.port.out.OptionReadPort;
import com.picus.core.price.domain.Option;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class OptionPersistenceAdapter implements OptionReadPort {

    private final OptionJpaRepository optionJpaRepository;
    private final OptionPersistenceMapper optionPersistenceMapper;

    @Override
    public Option findById(String optionNo) {
        OptionEntity entity = optionJpaRepository.findById(optionNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        return optionPersistenceMapper.toDomain(entity);
    }

    @Override
    public List<Option> findByIds(List<String> optionNos) {
        return optionJpaRepository.findByIds(optionNos).stream()
                .map(optionPersistenceMapper::toDomain)
                .toList();
    }
}
