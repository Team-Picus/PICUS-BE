package com.picus.core.price.adapter.in.web.data.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadPriceResponse(
        String priceNo,
        String priceThemeType,
        List<PriceReferenceImageResponse> priceReferenceImages,
        List<PackageResponse> packages,
        List<OptionResponse> options
) {

    @Builder
    public record PriceReferenceImageResponse(
        String priceRefImageNo,
        String imageUrl,
        Integer imageOrder
    ) {}

    @Builder
    public record PackageResponse(
        String packageNo,
        String name,
        Integer price,
        List<String> contents,
        String notice
    ) {}

    @Builder
    public record OptionResponse(
            String optionNo,
            String name,
            Integer count,
            Integer price,
            List<String> contents
    ) {}
}
