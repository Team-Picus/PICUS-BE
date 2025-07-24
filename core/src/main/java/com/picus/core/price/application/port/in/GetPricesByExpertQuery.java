package com.picus.core.price.application.port.in;

import com.picus.core.price.domain.model.Price;

import java.util.List;

public interface GetPricesByExpertQuery {

    List<Price> getPricesByExpert(String expertNo);
}
