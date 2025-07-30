package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;

import java.util.List;

public interface PriceReadPort {

    List<Price> findByExpertNo(String expertNo);

    Price findById(String priceNo);

}
