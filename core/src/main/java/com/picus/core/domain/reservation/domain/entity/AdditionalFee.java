package com.picus.core.domain.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer feeAmount;
    private String feeReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_no")
    private Reservation reservation;

    public AdditionalFee(Reservation reservation, Integer feeAmount, String feeReason) {
        if (feeAmount < 0) {
            throw new IllegalArgumentException("Fee amount must be non-negative");
        }
        if (feeReason == null || feeReason.isBlank()) {
            throw new IllegalArgumentException("Fee reason must be provided");
        }
        this.reservation = reservation;
        this.feeAmount = feeAmount;
        this.feeReason = feeReason;
    }
}
