package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.model.Price;

import java.util.List;

public interface LoadPricesByExpertPort {

    List<Price> loadPricesByExpert(String expertNo);


}
