package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatePriceReferenceImageRequest(
        String priceRefImageNo,
        @NotBlank String fileKey,
        @NotNull Integer imageOrder,
        @NotNull ChangeStatus status
) {
}
