package com.picus.core.domain.post.entity.pricing;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(OptionDiscountId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionDiscount {

    @Id
    private Long discountId;

    @Id
    private Long optionId;

    public OptionDiscount(Long discountId, Long optionId) {
        this.discountId = discountId;
        this.optionId = optionId;
    }
}
