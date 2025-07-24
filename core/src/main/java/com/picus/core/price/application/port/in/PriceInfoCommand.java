package com.picus.core.price.application.port.in;

import com.picus.core.price.application.port.in.request.PriceInfoCommandAppReq;

public interface PriceInfoCommand {
    void apply(PriceInfoCommandAppReq command, String currentUserNo);
}
