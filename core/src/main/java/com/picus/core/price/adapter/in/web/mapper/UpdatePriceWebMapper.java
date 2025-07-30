package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.command.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePriceWebMapper {

    public UpdatePriceListCommand toCommand(UpdatePriceListRequest webRequest) {
        return UpdatePriceListCommand.builder()
                .prices(toPriceCommand(webRequest.prices()))
                .build();
    }

    private List<UpdatePriceAppReq> toPriceCommand(List<UpdatePriceWebReq> updatePriceWebRequests) {
        if (updatePriceWebRequests == null || updatePriceWebRequests.isEmpty()) return List.of();
        return updatePriceWebRequests.stream()
                .map(w -> UpdatePriceAppReq.builder()
                        .priceNo(w.priceNo())
                        .priceThemeType(w.priceThemeType())
                        .priceReferenceImages(toPriceRefImageCommand(w.priceReferenceImages()))
                        .packages(toPackageCommand(w.packages()))
                        .options(toOptionCommand(w.options()))
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<UpdatePriceReferenceImageCommand> toPriceRefImageCommand(List<UpdatePriceReferenceImageRequest> webRequests) {
        if (webRequests == null || webRequests.isEmpty()) return List.of();
        return webRequests.stream()
                .map(w -> UpdatePriceReferenceImageCommand.builder()
                        .priceRefImageNo(w.priceRefImageNo())
                        .fileKey(w.fileKey())
                        .imageOrder(w.imageOrder())
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<UpdatePackageCommand> toPackageCommand(List<UpdatePackageRequest> updatePackageRequests) {
        if (updatePackageRequests == null || updatePackageRequests.isEmpty()) return List.of();
        return updatePackageRequests.stream()
                .map(w -> UpdatePackageCommand.builder()
                        .packageNo(w.packageNo())
                        .name(w.name())
                        .price(w.price())
                        .contents(w.contents())
                        .notice(w.notice())
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<UpdateOptionCommand> toOptionCommand(List<UpdateOptionRequest> updateOptionRequests) {
        if (updateOptionRequests == null || updateOptionRequests.isEmpty()) return List.of();
        return updateOptionRequests.stream()
                .map(w -> UpdateOptionCommand.builder()
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
