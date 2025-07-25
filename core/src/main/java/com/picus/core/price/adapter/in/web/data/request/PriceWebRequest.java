package com.picus.core.price.adapter.in.web.data.request;

import com.picus.core.price.application.port.in.command.ChangeStatus;
import com.picus.core.price.application.port.in.command.OptionCommand;
import com.picus.core.price.application.port.in.command.PackageCommand;
import com.picus.core.price.application.port.in.command.PriceReferenceImageCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record PriceWebRequest(
        String priceNo,
        String priceThemeType,
        @Valid List<PriceReferenceImageWebRequest> priceReferenceImages,
        @Valid List<PackageWebRequest> packages,
        @Valid List<OptionWebRequest> options,
        @NotNull ChangeStatus status
) {
}
