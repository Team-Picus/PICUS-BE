package com.picus.core.old.domain.post.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AdditionalOptionCreate(
        @NotEmpty String name,
        @NotNull Integer pricePerUnit,
        @NotNull Integer max,
        @NotNull Integer base,
        @NotNull Integer increment
) {}