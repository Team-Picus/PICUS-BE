package com.picus.core.price.application.port.in.command;

import java.util.List;

public record ApplyPriceChangesCommand (
        List<PriceCommand> prices
) {
}
