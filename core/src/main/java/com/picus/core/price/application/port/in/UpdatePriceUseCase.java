package com.picus.core.price.application.port.in;

import com.picus.core.price.application.port.in.request.UpdatePriceListCommand;

public interface UpdatePriceUseCase {
    void update(UpdatePriceListCommand command, String currentUserNo);
}
