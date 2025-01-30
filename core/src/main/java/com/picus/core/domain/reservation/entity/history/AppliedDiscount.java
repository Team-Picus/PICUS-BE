package com.picus.core.domain.reservation.entity.history;

import com.picus.core.domain.post.entity.pricing.DiscountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class AppliedDiscount {

    private String name;

    @Enumerated(value = EnumType.STRING)
    private DiscountType type;

    private double value;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}
