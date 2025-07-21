package com.picus.core.price.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record PackageCommand(
        String packageNo,
        String name,
        Integer price,
        List<String> contents,
        String notice,
        ChangeStatus status
) {
}
