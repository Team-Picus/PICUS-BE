package com.picus.core.domain.payments.entity;

import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Discount extends BaseEntity {

    @Id
    @Column(name = "discount_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType;

    private double discountValue;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}

