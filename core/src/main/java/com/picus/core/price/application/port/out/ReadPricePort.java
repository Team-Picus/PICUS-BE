package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;

import java.util.List;

public interface ReadPricePort {

    List<Price> findByExpertNo(String expertNo);

    Price findById(String priceNo);

}
