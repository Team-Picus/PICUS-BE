package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePackageRequest(
        String packageNo,
        @NotBlank String name,
        @NotNull Integer price,
        @NotNull @Size(min = 1) List<String> contents,
        @NotBlank String notice,
        @NotNull ChangeStatus status
) {
}
