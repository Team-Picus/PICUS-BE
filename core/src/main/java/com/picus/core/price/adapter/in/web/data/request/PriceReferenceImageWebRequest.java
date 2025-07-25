package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PriceReferenceImageWebRequest(
        String priceRefImageNo,
        String fileKey,
        Integer imageOrder,
        @NotNull ChangeStatus status
) {
}
