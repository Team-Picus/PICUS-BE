package com.picus.core.price.application.port.in;

import com.picus.core.price.application.port.in.request.UpdatePriceListAppReq;

public interface PriceInfoCommand {
    void updatePrice(UpdatePriceListAppReq command, String currentUserNo);
}
