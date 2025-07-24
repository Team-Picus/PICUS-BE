package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.request.OptionCommandAppReq;
import com.picus.core.price.domain.model.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionCommandAppMapper {

    public Option toDomain(OptionCommandAppReq optionCommandAppReq) {
        return Option.builder()
                .optionNo(optionCommandAppReq.optionNo())
                .name(optionCommandAppReq.name())
                .count(optionCommandAppReq.count())
                .price(optionCommandAppReq.price())
                .contents(optionCommandAppReq.contents())
                .build();
    }
}
