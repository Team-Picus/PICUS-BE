package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateOptionRequest(
        String optionNo,
        @NotBlank String name,
        @NotNull Integer count,
        @NotNull Integer price,
        List<String> contents,
        @NotNull ChangeStatus status
) {
}
