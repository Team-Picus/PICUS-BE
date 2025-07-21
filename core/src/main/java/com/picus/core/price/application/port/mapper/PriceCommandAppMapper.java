package com.picus.core.price.application.port.mapper;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.application.port.in.command.OptionCommand;
import com.picus.core.price.application.port.in.command.PackageCommand;
import com.picus.core.price.application.port.in.command.PriceCommand;
import com.picus.core.price.application.port.in.command.PriceReferenceImageCommand;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceCommandAppMapper {

    public Price toPriceDomain(PriceCommand priceCommand) {
        return Price.builder()
                .priceNo(priceCommand.priceNo())
                .priceThemeType(PriceThemeType.valueOf(priceCommand.priceThemeType()))
                .priceReferenceImages(toRefImageDomain(priceCommand.priceReferenceImages()))
                .packages(toPackageDomain(priceCommand.packages()))
                .options(toOptionDomain(priceCommand.options()))
                .build();
    }

    private List<PriceReferenceImage> toRefImageDomain(List<PriceReferenceImageCommand> priceRefImageCommands) {
        return priceRefImageCommands.stream()
                .map(priceRefImageCommand -> PriceReferenceImage.builder()
                        .priceRefImageNo(priceRefImageCommand.priceRefImageNo())
                        .fileKey(priceRefImageCommand.fileKey())
                        .imageOrder(priceRefImageCommand.imageOrder())
                        .build())
                .toList();
    }

    private List<Package> toPackageDomain(List<PackageCommand> packageCommands) {
        return packageCommands.stream()
                .map(packageCommand -> Package.builder()
                        .packageNo(packageCommand.packageNo())
                        .name(packageCommand.name())
                        .price(packageCommand.price())
                        .contents(packageCommand.contents())
                        .build())
                .toList();
    }

    private List<Option> toOptionDomain(List<OptionCommand> optionCommands) {
        return optionCommands.stream()
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
