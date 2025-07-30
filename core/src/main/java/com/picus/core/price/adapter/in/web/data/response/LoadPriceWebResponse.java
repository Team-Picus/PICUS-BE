package com.picus.core.price.adapter.in.web.data.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadPriceWebResponse(
        String priceNo,
        String priceThemeType,
        List<PriceReferenceImageWebResponse> priceReferenceImages,
        List<PackageWebResponse> packages,
        List<OptionWebResponse> options
) {

    @Builder
    public record PriceReferenceImageWebResponse(
        String priceRefImageNo,
        String imageUrl,
        Integer imageOrder
    ) {}

    @Builder
    public record PackageWebResponse(
        String packageNo,
        String name,
        Integer price,
        List<String> contents,
        String notice
    ) {}

    @Builder
    public record OptionWebResponse(
            String optionNo,
            String name,
            Integer count,
            Integer price,
            List<String> contents
    ) {}
}
