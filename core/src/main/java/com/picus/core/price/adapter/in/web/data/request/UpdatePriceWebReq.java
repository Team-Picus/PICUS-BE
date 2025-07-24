package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.request.ChangeStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceWebReq(
        String priceNo,
        String priceThemeType,
        @Valid List<UpdatePriceReferenceImageWebReq> priceReferenceImages,
        @Valid List<UpdatePackageWebReq> packages,
        @Valid List<UpdateOptionWebReq> options,
        @NotNull ChangeStatus status
) {
}
