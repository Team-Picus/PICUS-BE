package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.request.UpdateOptionAppReq;
import com.picus.core.price.domain.Option;
import org.springframework.stereotype.Component;

@Component
public class UpdateOptionAppMapper {

    public Option toDomain(UpdateOptionAppReq updateOptionAppReq) {
        return Option.builder()
                .optionNo(updateOptionAppReq.optionNo())
                .name(updateOptionAppReq.name())
                .count(updateOptionAppReq.count())
                .price(updateOptionAppReq.price())
                .contents(updateOptionAppReq.contents())
                .build();
    }
}
