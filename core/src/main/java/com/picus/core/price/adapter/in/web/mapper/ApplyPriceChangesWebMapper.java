package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.command.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyPriceChangesWebMapper {

    public ApplyPriceChangesCommand toCommand(ApplyPriceChangesWebRequest webRequest) {
        return ApplyPriceChangesCommand.builder()
                .prices(toPriceCommand(webRequest.prices()))
                .build();
    }

    private List<PriceCommand> toPriceCommand(List<PriceWebRequest> priceWebRequests) {
        return priceWebRequests.stream()
                .map(priceWebRequest -> PriceCommand.builder()
                        .priceNo(priceWebRequest.priceNo())
                        .priceThemeType(priceWebRequest.priceThemeType())
                        .priceReferenceImages(toPriceRefImageCommand(priceWebRequest.priceReferenceImages()))
                        .packages(toPackageCommand(priceWebRequest.packages()))
                        .options(toOptionCommand(priceWebRequest.options()))
                        .status(priceWebRequest.status())
                        .build()
                ).toList();
    }

    private List<PriceReferenceImageCommand> toPriceRefImageCommand(List<PriceReferenceImageWebRequest> priceReferenceImageWebRequests) {
        return priceReferenceImageWebRequests.stream()
                .map(webRequest -> PriceReferenceImageCommand.builder()
                        .priceRefImageNo(webRequest.priceRefImageNo())
                        .fileKey(webRequest.fileKey())
                        .imageOrder(webRequest.imageOrder())
                        .status(webRequest.status())
                        .build()
                ).toList();
    }

    private List<PackageCommand> toPackageCommand(List<PackageWebRequest> packageWebRequests) {
        return packageWebRequests.stream()
                .map(webRequest -> PackageCommand.builder()
                        .packageNo(webRequest.packageNo())
                        .name(webRequest.name())
                        .price(webRequest.price())
                        .contents(webRequest.contents())
                        .notice(webRequest.notice())
                        .status(webRequest.status())
                        .build()
                ).toList();
    }

    private List<OptionCommand> toOptionCommand(List<OptionWebRequest> optionWebRequests) {
        return optionWebRequests.stream()
                .map(webRequest -> OptionCommand.builder()
                        .optionNo(webRequest.optionNo())
                        .name(webRequest.name())
                        .count(webRequest.count())
                        .price(webRequest.price())
                        .contents(webRequest.contents())
                        .status(webRequest.status())
                        .build()
                ).toList();
    }
}
