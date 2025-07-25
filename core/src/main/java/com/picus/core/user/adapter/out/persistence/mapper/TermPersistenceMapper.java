package com.picus.core.user.adapter.out.persistence.mapper;

import com.picus.core.user.adapter.out.persistence.entity.TermEntity;
import com.picus.core.user.domain.model.Term;
import org.springframework.stereotype.Component;

@Component
public class TermPersistenceMapper {

    public Term toDomainModel(TermEntity entity) {
        return Term.builder()
                .termNo(entity.getTermNo())
                .name(entity.getName())
                .content(entity.getContent())
                .isRequired(entity.getIsRequired())
                .build();
    }
}
