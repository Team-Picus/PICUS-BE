package com.picus.core.price.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record PackageCommandAppReq(
        String packageNo,
        String name,
        Integer price,
        List<String> contents,
        String notice,
        ChangeStatus status
) {
}
