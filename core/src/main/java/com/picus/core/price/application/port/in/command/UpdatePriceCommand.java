package com.picus.core.price.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceCommand(
        String priceNo,
        String priceThemeType,
        List<UpdatePriceReferenceImageCommand> priceReferenceImages,
        List<UpdatePackageCommand> packages,
        List<UpdateOptionCommand> options,
        ChangeStatus status
) {
}
