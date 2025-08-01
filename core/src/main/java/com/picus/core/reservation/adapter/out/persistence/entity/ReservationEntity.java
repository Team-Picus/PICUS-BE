package com.picus.core.reservation.adapter.out.persistence.entity;

import com.picus.core.reservation.domain.ReservationStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationEntity {

    @Id @Tsid
    private String reservationNo;

    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private String requestDetail;

    // 주문 도메인


    @PrePersist
    protected void init() {
        this.reservationStatus = ReservationStatus.REQUESTED;
    }
}
