package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.application.port.out.LoadPricesByExpertPort;
import com.picus.core.price.domain.model.Price;
import com.picus.core.shared.annotation.PersistenceAdapter;

import java.util.List;

@PersistenceAdapter
public class PricePersistenceAdapter implements LoadPricesByExpertPort {
    @Override
    public List<Price> loadPricesByExpert(String expertNo) {
        return List.of();
    }
}
