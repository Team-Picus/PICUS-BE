package com.picus.core.price.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceAppReq(
        String priceNo,
        String priceThemeType,
        List<UpdatePriceReferenceImageCommand> priceReferenceImages,
        List<UpdatePackageCommand> packages,
        List<UpdateOptionCommand> options,
        ChangeStatus status
) {
}
