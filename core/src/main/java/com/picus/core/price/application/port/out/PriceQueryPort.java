package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.model.Price;

import java.util.List;

public interface PriceQueryPort {

    List<Price> findByExpertNo(String expertNo);


}
