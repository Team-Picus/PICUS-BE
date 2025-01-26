package com.picus.core.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OptionDiscount {

    @Id
    @GeneratedValue
    @Column(name = "option_discount_id")
    private Long id;

    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "option_id")
    private Long optionId;
}
