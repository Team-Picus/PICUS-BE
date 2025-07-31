package com.picus.core.price.application.port.in.command;

import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePriceCommand(
        String priceNo,
        PriceThemeType priceThemeType,
        SnapSubTheme snapSubTheme,
        List<UpdatePriceReferenceImageCommand> priceReferenceImages,
        List<UpdatePackageCommand> packages,
        List<UpdateOptionCommand> options,
        ChangeStatus status
) {
}
