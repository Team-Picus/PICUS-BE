package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.command.OptionCommand;
import com.picus.core.price.domain.model.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionCommandAppMapper {

    public Option toDomain(OptionCommand optionCommand) {
        return Option.builder()
                .optionNo(optionCommand.optionsNo())
                .name(optionCommand.name())
                .count(optionCommand.count())
                .price(optionCommand.price())
                .contents(optionCommand.contents())
                .build();
    }
}
