package com.picus.core.price.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record ApplyPriceChangesCommand (
        List<PriceCommand> prices
) {
}
