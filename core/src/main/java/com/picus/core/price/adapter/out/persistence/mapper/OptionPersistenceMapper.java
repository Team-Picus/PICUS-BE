package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.domain.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionPersistenceMapper {

    public Option toDomain(OptionEntity optionEntity) {
        return Option.builder()
                .optionNo(optionEntity.getOptionNo())
                .name(optionEntity.getName())
                .unitSize(optionEntity.getUnitSize())
                .pricePerUnit(optionEntity.getPricePerUnit())
                .contents(optionEntity.getContents())
                .build();
    }

    public OptionEntity toEntity(Option option) {
        return OptionEntity.builder()
                .name(option.getName())
                .unitSize(option.getUnitSize())
                .pricePerUnit(option.getPricePerUnit())
                .contents(option.getContents())
                .build();
    }
}
