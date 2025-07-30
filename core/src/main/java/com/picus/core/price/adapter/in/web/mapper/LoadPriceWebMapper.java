package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse.OptionResponse;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse.PackageResponse;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse.PriceReferenceImageResponse;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadPriceWebMapper {

    public LoadPriceResponse toResponse(Price price) {
        return LoadPriceResponse.builder()
                .priceNo(price.getPriceNo())
                .priceThemeType(price.getPriceThemeType().toString())
                .priceReferenceImages(toPriceReferenceImageResponse(price.getPriceReferenceImages()))
                .packages(toPackageResponse(price.getPackages()))
                .options(toOptionResponse(price.getOptions()))
                .build();
    }

    private List<PriceReferenceImageResponse> toPriceReferenceImageResponse(List<PriceReferenceImage> priceReferenceImages) {
        return priceReferenceImages.stream()
                .map(priceReferenceImage -> PriceReferenceImageResponse.builder()
                        .priceRefImageNo(priceReferenceImage.getPriceRefImageNo())
                        .imageUrl(priceReferenceImage.getImageUrl())
                        .imageOrder(priceReferenceImage.getImageOrder())
                        .build()
                ).toList();
    }

    private List<PackageResponse> toPackageResponse(List<Package> packages) {
        return packages.stream()
                .map(p -> PackageResponse.builder()
                        .packageNo(p.getPackageNo())
                        .name(p.getName())
                        .price(p.getPrice())
                        .contents(p.getContents())
                        .notice(p.getNotice())
                        .build()
                ).toList();
    }

    private List<OptionResponse> toOptionResponse(List<Option> options) {
        return options.stream()
                .map(option -> OptionResponse.builder()
                        .optionNo(option.getOptionNo())
                        .name(option.getName())
                        .count(option.getCount())
                        .price(option.getPrice())
                        .contents(option.getContents())
                        .build()
                ).toList();
    }
}
