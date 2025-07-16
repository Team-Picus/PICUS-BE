package com.picus.core.price.domain.model;

import com.picus.core.expert.domain.model.vo.PriceThemeType;

import java.util.ArrayList;
import java.util.List;

public class Price {

    private String priceNo;

    private String expertNo;
    private PriceThemeType priceThemeType;
    private List<PriceReferenceImage> priceReferenceImages = new ArrayList<>();
    private List<Package> packages;
    private List<Option> options;
}
