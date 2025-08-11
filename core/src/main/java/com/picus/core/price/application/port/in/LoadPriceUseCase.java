package com.picus.core.price.application.port.in;

import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;

import java.util.List;

public interface LoadPriceUseCase {

    List<Price> loadByExpertNo(String expertNo, List<PriceThemeType> priceThemeTypes, List<SnapSubTheme> snapSubThemes);
}
