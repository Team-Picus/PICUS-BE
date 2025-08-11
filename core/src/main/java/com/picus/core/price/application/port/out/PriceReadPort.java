package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;

import java.util.List;

public interface PriceReadPort {

    List<Price> findByExpertNoAndThemes(String expertNo, List<PriceThemeType> priceThemeTypes, List<SnapSubTheme> snapSubThemes);

    List<Price> findByExpertNo(String expertNo);

    Price findById(String priceNo);

}
