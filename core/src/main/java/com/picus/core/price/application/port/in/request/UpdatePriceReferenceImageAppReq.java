package com.picus.core.price.application.port.in.request;

import lombok.Builder;

@Builder
public record UpdatePriceReferenceImageAppReq(
        String priceRefImageNo,
        String fileKey,
        Integer imageOrder,
        ChangeStatus status
) {
}
