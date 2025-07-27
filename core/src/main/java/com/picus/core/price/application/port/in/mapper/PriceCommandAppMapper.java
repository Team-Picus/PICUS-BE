package com.picus.core.price.application.port.in.mapper;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.price.application.port.in.request.UpdateOptionAppReq;
import com.picus.core.price.application.port.in.request.UpdatePackageAppReq;
import com.picus.core.price.application.port.in.request.UpdatePriceAppReq;
import com.picus.core.price.application.port.in.request.UpdatePriceReferenceImageAppReq;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceCommandAppMapper {

    public Price toPriceDomain(UpdatePriceAppReq updatePriceAppReq) {
        return Price.builder()
                .priceNo(updatePriceAppReq.priceNo())
                .priceThemeType(PriceThemeType.valueOf(updatePriceAppReq.priceThemeType()))
                .priceReferenceImages(toRefImageDomain(updatePriceAppReq.priceReferenceImages()))
                .packages(toPackageDomain(updatePriceAppReq.packages()))
                .options(toOptionDomain(updatePriceAppReq.options()))
                .build();
    }

    private List<PriceReferenceImage> toRefImageDomain(List<UpdatePriceReferenceImageAppReq> priceRefImageCommands) {
        return priceRefImageCommands.stream()
                .map(priceRefImageCommand -> PriceReferenceImage.builder()
                        .priceRefImageNo(priceRefImageCommand.priceRefImageNo())
                        .fileKey(priceRefImageCommand.fileKey())
                        .imageOrder(priceRefImageCommand.imageOrder())
                        .build())
                .toList();
    }

    private List<Package> toPackageDomain(List<UpdatePackageAppReq> updatePackageAppReqs) {
        return updatePackageAppReqs.stream()
                .map(packageCommand -> Package.builder()
                        .packageNo(packageCommand.packageNo())
                        .name(packageCommand.name())
                        .price(packageCommand.price())
                        .contents(packageCommand.contents())
                        .notice(packageCommand.notice())
                        .build())
                .toList();
    }

    private List<Option> toOptionDomain(List<UpdateOptionAppReq> updateOptionAppReqs) {
        return updateOptionAppReqs.stream()
                .map(optionCommand -> Option.builder()
                        .optionNo(optionCommand.optionNo())
                        .name(optionCommand.name())
                        .count(optionCommand.count())
                        .price(optionCommand.price())
                        .contents(optionCommand.contents())
                        .build())
                .toList();
    }
}
