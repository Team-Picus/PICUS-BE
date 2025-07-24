package com.picus.core.price.application.port.mapper;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.application.port.in.request.OptionCommandAppReq;
import com.picus.core.price.application.port.in.request.PackageCommandAppReq;
import com.picus.core.price.application.port.in.request.PriceCommandAppReq;
import com.picus.core.price.application.port.in.request.PriceReferenceImageCommandAppReq;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceCommandAppMapper {

    public Price toPriceDomain(PriceCommandAppReq priceCommandAppReq) {
        return Price.builder()
                .priceNo(priceCommandAppReq.priceNo())
                .priceThemeType(PriceThemeType.valueOf(priceCommandAppReq.priceThemeType()))
                .priceReferenceImages(toRefImageDomain(priceCommandAppReq.priceReferenceImages()))
                .packages(toPackageDomain(priceCommandAppReq.packages()))
                .options(toOptionDomain(priceCommandAppReq.options()))
                .build();
    }

    private List<PriceReferenceImage> toRefImageDomain(List<PriceReferenceImageCommandAppReq> priceRefImageCommands) {
        return priceRefImageCommands.stream()
                .map(priceRefImageCommand -> PriceReferenceImage.builder()
                        .priceRefImageNo(priceRefImageCommand.priceRefImageNo())
                        .fileKey(priceRefImageCommand.fileKey())
                        .imageOrder(priceRefImageCommand.imageOrder())
                        .build())
                .toList();
    }

    private List<Package> toPackageDomain(List<PackageCommandAppReq> packageCommandAppReqs) {
        return packageCommandAppReqs.stream()
                .map(packageCommand -> Package.builder()
                        .packageNo(packageCommand.packageNo())
                        .name(packageCommand.name())
                        .price(packageCommand.price())
                        .contents(packageCommand.contents())
                        .notice(packageCommand.notice())
                        .build())
                .toList();
    }

    private List<Option> toOptionDomain(List<OptionCommandAppReq> optionCommandAppReqs) {
        return optionCommandAppReqs.stream()
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
