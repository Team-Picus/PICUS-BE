package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.command.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplyPriceChangesWebMapper {

    public ApplyPriceChangesCommand toCommand(ApplyPriceChangesWebRequest webRequest) {
        return ApplyPriceChangesCommand.builder()
                .prices(toPriceCommand(webRequest.prices()))
                .build();
    }

    private List<PriceCommand> toPriceCommand(List<PriceWebRequest> priceWebRequests) {
        if (priceWebRequests == null || priceWebRequests.isEmpty()) return List.of();
        return priceWebRequests.stream()
                .map(w -> PriceCommand.builder()
                        .priceNo(w.priceNo())
                        .priceThemeType(w.priceThemeType())
                        .priceReferenceImages(toPriceRefImageCommand(w.priceReferenceImages()))
                        .packages(toPackageCommand(w.packages()))
                        .options(toOptionCommand(w.options()))
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<PriceReferenceImageCommand> toPriceRefImageCommand(List<PriceReferenceImageWebRequest> webRequests) {
        if (webRequests == null || webRequests.isEmpty()) return List.of();
        return webRequests.stream()
                .map(w -> PriceReferenceImageCommand.builder()
                        .priceRefImageNo(w.priceRefImageNo())
                        .fileKey(w.fileKey())
                        .imageOrder(w.imageOrder())
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<PackageCommand> toPackageCommand(List<PackageWebRequest> packageWebRequests) {
        if (packageWebRequests == null || packageWebRequests.isEmpty()) return List.of();
        return packageWebRequests.stream()
                .map(w -> PackageCommand.builder()
                        .packageNo(w.packageNo())
                        .name(w.name())
                        .price(w.price())
                        .contents(w.contents())
                        .notice(w.notice())
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<OptionCommand> toOptionCommand(List<OptionWebRequest> optionWebRequests) {
        if (optionWebRequests == null || optionWebRequests.isEmpty()) return List.of();
        return optionWebRequests.stream()
                .map(w -> OptionCommand.builder()
                        .optionNo(w.optionNo())
                        .name(w.name())
                        .count(w.count())
                        .price(w.price())
                        .contents(w.contents())
                        .status(w.status())
                        .build())
                .toList();
    }
}
