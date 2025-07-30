package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;

public interface PriceCreatePort {
    Price create(Price price, String expertNo);
}
