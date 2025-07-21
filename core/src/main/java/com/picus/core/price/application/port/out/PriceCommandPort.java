package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.model.Price;

public interface PriceCommandPort {
    Price save(Price price, String expertNo);

    void delete(String priceNo);

    void update(Price price);
}
