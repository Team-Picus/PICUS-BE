package com.picus.core.price.adapter.in.web.data.response;

import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import lombok.Builder;

import java.util.List;

@Builder
public record LoadPriceResponse(
    List<PriceResponse> prices
) {
    @Builder
    public record PriceResponse(
            String priceNo,
            PriceThemeType priceThemeType,
            SnapSubTheme snapSubTheme,
            List<PriceReferenceImageResponse> priceReferenceImages,
            List<PackageResponse> packages,
            List<OptionResponse> options
    ) {

        @Builder
        public record PriceReferenceImageResponse(
                String priceRefImageNo,
                String fileKey,
                String imageUrl,
                Integer imageOrder
        ) {
        }

        @Builder
        public record PackageResponse(
                String packageNo,
                String name,
                Integer price,
                List<String> contents,
                String notice
        ) {
        }

        @Builder
        public record OptionResponse(
                String optionNo,
                String name,
                Integer count,
                Integer price,
                List<String> contents
        ) {
        }
    }
}
