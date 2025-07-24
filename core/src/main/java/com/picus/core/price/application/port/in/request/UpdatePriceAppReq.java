package com.picus.core.price.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceAppReq(
        String priceNo,
        String priceThemeType,
        List<UpdatePriceReferenceImageAppReq> priceReferenceImages,
        List<UpdatePackageAppReq> packages,
        List<UpdateOptionAppReq> options,
        ChangeStatus status
) {
}
