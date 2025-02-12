package com.picus.core.domain.payments.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OptionDiscountId implements Serializable {

    @EqualsAndHashCode.Include
    private Long optionId;

    @EqualsAndHashCode.Include
    private Long discountId;

    public OptionDiscountId(Long optionId, Long discountId) {
        this.optionId = optionId;
        this.discountId = discountId;
    }
}
