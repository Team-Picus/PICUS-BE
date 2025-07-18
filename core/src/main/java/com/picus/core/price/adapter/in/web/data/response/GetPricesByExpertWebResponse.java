package com.picus.core.price.adapter.in.web.data.response;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.domain.model.PriceReferenceImage;
import lombok.Builder;

import java.util.List;

@Builder
public record GetPricesByExpertWebResponse(
        String priceNo,
        String priceThemeType,
        List<PriceReferenceImageWebResponse> priceReferenceImages,
        List<PackageWebResponse> packages,
        List<OptionWebResponse> options
) {

    @Builder
    public record PriceReferenceImageWebResponse(
        String imageUrl,
        Integer imageOrder
    ) {}

    @Builder
    public record PackageWebResponse(
        String name,
        Integer price,
        List<String> contents,
        String notice
    ) {}

    @Builder
    public record OptionWebResponse(
            String name,
            Integer count,
            Integer price,
            List<String> contents
    ) {}
}
