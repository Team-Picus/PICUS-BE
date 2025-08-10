package com.picus.core.price.adapter.in.web.data.response;

import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import lombok.Builder;

import java.util.List;

@Builder
public record LoadMyPackageResponse(
    List<PriceResponse> prices
) {
    @Builder
    public record PriceResponse(
            PriceThemeType priceThemeType,
            SnapSubTheme snapSubTheme,
            List<PackageResponse> packages
    ) {
        @Builder
        public record PackageResponse(
                String packageNo,
                String name
        ) {
        }
    }
}
