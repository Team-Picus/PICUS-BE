package com.picus.core.price.domain.model;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class Price {

    private String priceNo;

    private PriceThemeType priceThemeType;
    private List<PriceReferenceImage> priceReferenceImages;
    private List<Package> packages;
    private List<Option> options;
}
