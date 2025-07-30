package com.picus.core.price.application.port.in.mapper;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.price.application.port.in.request.UpdateOptionCommand;
import com.picus.core.price.application.port.in.request.UpdatePackageCommand;
import com.picus.core.price.application.port.in.request.UpdatePriceAppReq;
import com.picus.core.price.application.port.in.request.UpdatePriceReferenceImageCommand;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePriceAppMapper {

    public Price toPriceDomain(UpdatePriceAppReq updatePriceAppReq) {
        return Price.builder()
                .priceNo(updatePriceAppReq.priceNo())
                .priceThemeType(PriceThemeType.valueOf(updatePriceAppReq.priceThemeType()))
                .priceReferenceImages(toRefImageDomain(updatePriceAppReq.priceReferenceImages()))
                .packages(toPackageDomain(updatePriceAppReq.packages()))
                .options(toOptionDomain(updatePriceAppReq.options()))
                .build();
    }

    private List<PriceReferenceImage> toRefImageDomain(List<UpdatePriceReferenceImageCommand> priceRefImageCommands) {
        return priceRefImageCommands.stream()
                .map(priceRefImageCommand -> PriceReferenceImage.builder()
                        .priceRefImageNo(priceRefImageCommand.priceRefImageNo())
                        .fileKey(priceRefImageCommand.fileKey())
                        .imageOrder(priceRefImageCommand.imageOrder())
                        .build())
                .toList();
    }

    private List<Package> toPackageDomain(List<UpdatePackageCommand> updatePackageCommands) {
        return updatePackageCommands.stream()
                .map(packageCommand -> Package.builder()
                        .packageNo(packageCommand.packageNo())
                        .name(packageCommand.name())
                        .price(packageCommand.price())
                        .contents(packageCommand.contents())
                        .notice(packageCommand.notice())
                        .build())
                .toList();
    }

    private List<Option> toOptionDomain(List<UpdateOptionCommand> updateOptionCommands) {
        return updateOptionCommands.stream()
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
