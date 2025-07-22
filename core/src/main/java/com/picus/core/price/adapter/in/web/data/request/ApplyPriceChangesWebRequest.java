package com.picus.core.price.adapter.in.web.data.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplyPriceChangesWebRequest(
    @NotNull @Valid List<PriceWebRequest> prices
) {}
