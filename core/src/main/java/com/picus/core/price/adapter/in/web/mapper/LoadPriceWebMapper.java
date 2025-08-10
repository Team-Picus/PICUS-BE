package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadPriceWebMapper {

    public LoadPriceResponse toResponse(List<Price> prices) {
        return LoadPriceResponse.builder()
                .prices(toPriceResponse(prices))
                .build();
    }

    private List<LoadPriceResponse.PriceResponse> toPriceResponse(List<Price> prices) {
        return prices.stream()
                .map(price -> LoadPriceResponse.PriceResponse.builder()
                        .priceNo(price.getPriceNo())
                        .priceThemeType(price.getPriceThemeType())
                        .snapSubTheme(price.getSnapSubTheme())
                        .priceReferenceImages(toPriceReferenceImageResponse(price.getPriceReferenceImages()))
                        .packages(toPackageResponse(price.getPackages()))
                        .options(toOptionResponse(price.getOptions()))
                        .build()
                ).toList();
    }


    private List<LoadPriceResponse.PriceResponse.PriceReferenceImageResponse> toPriceReferenceImageResponse(List<PriceReferenceImage> priceReferenceImages) {
        return priceReferenceImages.stream()
                .map(priceReferenceImage -> LoadPriceResponse.PriceResponse.PriceReferenceImageResponse.builder()
                        .priceRefImageNo(priceReferenceImage.getPriceRefImageNo())
                        .fileKey(priceReferenceImage.getFileKey())
                        .imageUrl(priceReferenceImage.getImageUrl())
                        .imageOrder(priceReferenceImage.getImageOrder())
                        .build()
                ).toList();
    }

    private List<LoadPriceResponse.PriceResponse.PackageResponse> toPackageResponse(List<Package> packages) {
        return packages.stream()
                .map(p -> LoadPriceResponse.PriceResponse.PackageResponse.builder()
                        .packageNo(p.getPackageNo())
                        .name(p.getName())
                        .price(p.getPrice())
                        .contents(p.getContents())
                        .notice(p.getNotice())
                        .build()
                ).toList();
    }

    private List<LoadPriceResponse.PriceResponse.OptionResponse> toOptionResponse(List<Option> options) {
        return options.stream()
                .map(option -> LoadPriceResponse.PriceResponse.OptionResponse.builder()
                        .optionNo(option.getOptionNo())
                        .name(option.getName())
                        .count(option.getUnitSize())
                        .price(option.getPricePerUnit())
                        .contents(option.getContents())
                        .build()
                ).toList();
    }
}
