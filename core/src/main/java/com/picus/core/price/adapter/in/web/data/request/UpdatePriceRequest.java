package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceRequest(
        String priceNo,
        @NotNull PriceThemeType priceThemeType,
        SnapSubTheme snapSubTheme,
        @Valid @Size(min = 1) List<UpdatePriceReferenceImageRequest> priceReferenceImages,
        @Valid @Size(min = 1) List<UpdatePackageRequest> packages,
        @Valid @Size(min = 1) List<UpdateOptionRequest> options,
        @NotNull ChangeStatus status
) {
}
