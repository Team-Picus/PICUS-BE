package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import com.picus.core.price.application.port.in.command.OptionCommand;
import com.picus.core.price.application.port.in.command.PackageCommand;
import com.picus.core.price.application.port.in.command.PriceReferenceImageCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record PriceWebRequest(
        String priceNo,
        String priceThemeType,
        List<PriceReferenceImageWebRequest> priceReferenceImages,
        List<PackageWebRequest> packages,
        List<OptionWebRequest> options,
        @NotNull ChangeStatus status
) {
}
