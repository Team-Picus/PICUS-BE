package com.picus.core.price.application.port.in;

import com.picus.core.price.domain.Price;

import java.util.List;

public interface LoadPriceUseCase {

    List<Price> loadByExpertNo(String expertNo);
}
