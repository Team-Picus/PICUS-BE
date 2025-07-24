package com.picus.core.price.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record PriceInfoCommandAppReq(
        List<PriceCommandAppReq> prices
) {
}
