package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;

import java.util.List;

public interface CreatePricePort {
    Price create(Price price, String expertNo);
}
