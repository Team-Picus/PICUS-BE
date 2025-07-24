package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.request.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePriceWebMapper {

    public UpdatePriceListAppReq toCommand(UpdatePriceListWebReq webRequest) {
        return UpdatePriceListAppReq.builder()
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

    private List<UpdatePriceReferenceImageAppReq> toPriceRefImageCommand(List<UpdatePriceReferenceImageWebReq> webRequests) {
        if (webRequests == null || webRequests.isEmpty()) return List.of();
        return webRequests.stream()
                .map(w -> UpdatePriceReferenceImageAppReq.builder()
                        .priceRefImageNo(w.priceRefImageNo())
                        .fileKey(w.fileKey())
                        .imageOrder(w.imageOrder())
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<UpdatePackageAppReq> toPackageCommand(List<UpdatePackageWebReq> updatePackageWebReqs) {
        if (updatePackageWebReqs == null || updatePackageWebReqs.isEmpty()) return List.of();
        return updatePackageWebReqs.stream()
                .map(w -> UpdatePackageAppReq.builder()
                        .packageNo(w.packageNo())
                        .name(w.name())
                        .price(w.price())
                        .contents(w.contents())
                        .notice(w.notice())
                        .status(w.status())
                        .build())
                .toList();
    }

    private List<UpdateOptionAppReq> toOptionCommand(List<UpdateOptionWebReq> updateOptionWebReqs) {
        if (updateOptionWebReqs == null || updateOptionWebReqs.isEmpty()) return List.of();
        return updateOptionWebReqs.stream()
                .map(w -> UpdateOptionAppReq.builder()
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
