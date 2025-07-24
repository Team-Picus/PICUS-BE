package com.picus.core.price.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record PriceCommandAppReq(
        String priceNo,
        String priceThemeType,
        List<PriceReferenceImageCommandAppReq> priceReferenceImages,
        List<PackageCommandAppReq> packages,
        List<OptionCommandAppReq> options,
        ChangeStatus status
) {
}
