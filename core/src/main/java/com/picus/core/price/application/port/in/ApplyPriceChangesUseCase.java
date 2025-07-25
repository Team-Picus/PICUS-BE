package com.picus.core.price.application.port.in;

import com.picus.core.price.application.port.in.command.ApplyPriceChangesCommand;

public interface ApplyPriceChangesUseCase {
    void apply(ApplyPriceChangesCommand command, String currentUserNo);
}
