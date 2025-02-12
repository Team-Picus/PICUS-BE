package com.picus.core.domain.payments.entity;

import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(OptionDiscountId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionDiscount extends BaseEntity {

    @Id
    private Long discountId;

    @Id
    private Long optionId;

    public OptionDiscount(Long discountId, Long optionId) {
        this.discountId = discountId;
        this.optionId = optionId;
    }
}
