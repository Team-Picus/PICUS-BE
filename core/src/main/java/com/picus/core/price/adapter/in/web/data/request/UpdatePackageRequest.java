package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePackageRequest(
        String packageNo,
        String name,
        Integer price,
        List<String> contents,
        String notice,
        @NotNull ChangeStatus status
) {
}
