package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.domain.model.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionPersistenceMapper {

    public Option toDomain(OptionEntity optionEntity) {
        return Option.builder()
                .name(optionEntity.getName())
                .count(optionEntity.getCount())
                .price(optionEntity.getPrice())
                .content(optionEntity.getContent())
                .build();
    }
}
