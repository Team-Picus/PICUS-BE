package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.request.ChangeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatePriceReferenceImageWebReq(
        String priceRefImageNo,
        String fileKey,
        Integer imageOrder,
        @NotNull ChangeStatus status
) {
}
