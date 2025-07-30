package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.command.UpdateOptionCommand;
import com.picus.core.price.domain.Option;
import org.springframework.stereotype.Component;

@Component
public class UpdateOptionCommandMapper {

    public Option toDomain(UpdateOptionCommand updateOptionCommand) {
        return Option.builder()
                .optionNo(updateOptionCommand.optionNo())
                .name(updateOptionCommand.name())
                .count(updateOptionCommand.count())
                .price(updateOptionCommand.price())
                .contents(updateOptionCommand.contents())
                .build();
    }
}
