package com.picus.core.price.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateOptionAppReq(
        String optionNo,
        String name,
        Integer count,
        Integer price,
        List<String> contents,
        ChangeStatus status
) {
}
