package com.picus.core.domain.post.entity.pricing;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Discount {
    @Id
    @GeneratedValue
    private Long discountId;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType;
    private double discountValue;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}

