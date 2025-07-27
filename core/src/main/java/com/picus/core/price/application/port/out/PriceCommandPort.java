package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;

import java.util.List;

public interface PriceCommandPort {
    Price save(Price price, String expertNo);

    void delete(String priceNo);

    void update(Price price,
                List<String> deletedPriceRefImageNos, List<String> deletedPackageNos, List<String> deletedOptionNos);
}
