package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.response.LoadPriceWebResponse;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceWebResponse.OptionWebResponse;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceWebResponse.PackageWebResponse;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceWebResponse.PriceReferenceImageWebResponse;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadPriceWebMapper {

    public LoadPriceWebResponse toWebResponse(Price price) {
        return LoadPriceWebResponse.builder()
                .priceNo(price.getPriceNo())
                .priceThemeType(price.getPriceThemeType().toString())
                .priceReferenceImages(toPriceReferenceImageWebResponse(price.getPriceReferenceImages()))
                .packages(toPackageWebResponse(price.getPackages()))
                .options(toOptionWebResponse(price.getOptions()))
                .build();
    }

    private List<PriceReferenceImageWebResponse> toPriceReferenceImageWebResponse(List<PriceReferenceImage> priceReferenceImages) {
        return priceReferenceImages.stream()
                .map(priceReferenceImage -> PriceReferenceImageWebResponse.builder()
                        .priceRefImageNo(priceReferenceImage.getPriceRefImageNo())
                        .imageUrl(priceReferenceImage.getImageUrl())
                        .imageOrder(priceReferenceImage.getImageOrder())
                        .build()
                ).toList();
    }

    private List<PackageWebResponse> toPackageWebResponse(List<Package> packages) {
        return packages.stream()
                .map(p -> PackageWebResponse.builder()
                        .packageNo(p.getPackageNo())
                        .name(p.getName())
                        .price(p.getPrice())
                        .contents(p.getContents())
                        .notice(p.getNotice())
                        .build()
                ).toList();
    }

    private List<OptionWebResponse> toOptionWebResponse(List<Option> options) {
        return options.stream()
                .map(option -> OptionWebResponse.builder()
                        .optionNo(option.getOptionNo())
                        .name(option.getName())
                        .count(option.getCount())
                        .price(option.getPrice())
                        .contents(option.getContents())
                        .build()
                ).toList();
    }
}
