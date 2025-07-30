package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceRequest(
        String priceNo,
        String priceThemeType,
        @Valid List<UpdatePriceReferenceImageRequest> priceReferenceImages,
        @Valid List<UpdatePackageRequest> packages,
        @Valid List<UpdateOptionRequest> options,
        @NotNull ChangeStatus status
) {
}
