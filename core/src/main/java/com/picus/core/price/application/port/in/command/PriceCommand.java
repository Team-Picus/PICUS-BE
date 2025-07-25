package com.picus.core.price.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record PriceCommand(
        String priceNo,
        String priceThemeType,
        List<PriceReferenceImageCommand> priceReferenceImages,
        List<PackageCommand> packages,
        List<OptionCommand> options,
        ChangeStatus status
) {
}
