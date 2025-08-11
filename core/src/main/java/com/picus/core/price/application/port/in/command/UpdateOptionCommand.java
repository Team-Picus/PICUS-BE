package com.picus.core.price.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateOptionCommand(
        String optionNo,
        String name,
        Integer count,
        Integer price,
        List<String> contents,
        ChangeStatus status
) {
}
