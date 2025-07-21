package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.domain.model.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionPersistenceMapper {

    public Option toDomain(OptionEntity optionEntity) {
        return Option.builder()
                .optionNo(optionEntity.getOptionNo())
                .name(optionEntity.getName())
                .count(optionEntity.getCount())
                .price(optionEntity.getPrice())
                .contents(optionEntity.getContents())
                .build();
    }

    public OptionEntity toEntity(Option option) {
        return OptionEntity.builder()
                .name(option.getName())
                .count(option.getCount())
                .price(option.getPrice())
                .contents(option.getContents())
                .build();
    }
}
