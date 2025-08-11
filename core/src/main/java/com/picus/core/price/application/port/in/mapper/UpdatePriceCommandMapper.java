package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.command.UpdateOptionCommand;
import com.picus.core.price.application.port.in.command.UpdatePackageCommand;
import com.picus.core.price.application.port.in.command.UpdatePriceCommand;
import com.picus.core.price.application.port.in.command.UpdatePriceReferenceImageCommand;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePriceCommandMapper {

    public Price toPriceDomain(UpdatePriceCommand updatePriceCommand) {
        return Price.builder()
                .priceNo(updatePriceCommand.priceNo())
                .priceThemeType(updatePriceCommand.priceThemeType())
                .snapSubTheme(updatePriceCommand.snapSubTheme())
                .priceReferenceImages(toRefImageDomain(updatePriceCommand.priceReferenceImages()))
                .packages(toPackageDomain(updatePriceCommand.packages()))
                .options(toOptionDomain(updatePriceCommand.options()))
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
                        .unitSize(optionCommand.count())
                        .pricePerUnit(optionCommand.price())
                        .contents(optionCommand.contents())
                        .build())
                .toList();
    }
}
