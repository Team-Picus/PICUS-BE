package com.picus.core.price.application.port.in.command;

import lombok.Builder;

@Builder
public record PriceReferenceImageCommand(
        String priceRefImageNo,
        String fileKey,
        int imageOrder,
        ChangeStatus status
) {
}
