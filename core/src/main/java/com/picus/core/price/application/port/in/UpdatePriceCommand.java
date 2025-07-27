package com.picus.core.price.application.port.in;

import com.picus.core.price.application.port.in.request.UpdatePriceListAppReq;

public interface UpdatePriceCommand {
    void update(UpdatePriceListAppReq command, String currentUserNo);
}
