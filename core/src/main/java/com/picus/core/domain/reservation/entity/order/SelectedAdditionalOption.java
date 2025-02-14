package com.picus.core.domain.reservation.entity.order;

import com.picus.core.domain.post.entity.pricing.OptionType;

public class SelectedAdditionalOption {

    // option capture
    private Integer pricePerUnit;

    private Integer base;

    private Integer increment;

    private OptionType optionType;

    // client selection
    private Integer count;

    // result
    private Integer totalAmount;    // count * increment + base

    private Integer totalAdditionalPrice;   // pricePerUnit * count
}
