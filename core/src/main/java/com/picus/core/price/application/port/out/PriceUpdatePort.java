package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;

import java.util.List;

public interface PriceUpdatePort {
    void update(Price price,
                List<String> deletedPriceRefImageNos, List<String> deletedPackageNos, List<String> deletedOptionNos);
}
